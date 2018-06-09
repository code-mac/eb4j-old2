/*
 * Copyright Apehat.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apehat.eb4j;

import com.apehat.eb4j.event.*;
import com.apehat.eb4j.poster.DefaultPosterFactory;
import com.apehat.eb4j.poster.Poster;
import com.apehat.eb4j.poster.PosterFactory;
import com.apehat.eb4j.subscription.*;
import com.apehat.store.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 建立事件的防止多次发布，可以通过事件的Tag进行记录
 *
 * @author hanpengfei
 * @since 1.0
 */
public final class EventBus {

    /** Register lock, only be used by {@link #register(Subscriber)} */
    private final ReentrantLock registerLock = new ReentrantLock();

    /** Post lock, only be used by {@link #publish(Event)} */
    private final ReentrantLock postLock = new ReentrantLock();

    private final ReentrantLock publishLock = new ReentrantLock();

    /**
     * The subscriber filter, be used to filter subscribers.
     * <p>
     * May can open this filed to user;
     * Mat can use filter chain, too
     */
    private final SubscriberFilter subscriberFilter = new EventTypeFilter();

    private final Poster syncPoster;
    private final Poster asyncPoster;

    /** The subscriber store accessor, be used to access subscriber store */
    private final SubscriberStoreAccessor subscriberStoreAccessor;
    private final EventQueueAccessor eventQueueAccessor;
    /**
     * Event cache. This will be used to cache submitted event when {@code pendingEvents} be
     * locked.
     */
    private CacheAccessor<Event> eventCacheAccessor;
    /**
     * Subscriber cache. This will be used to cache isRegistered subscriber when {@code
     * subscriberStore} be locked.
     */
    private CacheAccessor<Subscriber> subscriberCacheAccessor;

    private EventBus(EventBus.Builder builder) {
        final String accessToken = UUID.randomUUID().toString();
        // by use private access token, ensure store cannot be access by others
        try {
            this.eventCacheAccessor = builder.eventCacheFactory.newCache(accessToken).getAccessor(accessToken);
            this.eventQueueAccessor = builder.eventQueueFactory.newStore(accessToken).getAccessor(accessToken);
            this.subscriberCacheAccessor = builder.subscriberCacheFactory.newCache(accessToken)
                                                                         .getAccessor(accessToken);
            this.subscriberStoreAccessor = builder.subscriberStoreFactory.newStore(accessToken)
                                                                         .getAccessor(accessToken);
        } catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
        this.syncPoster = builder.posterFactory.syncPoster();
        this.asyncPoster = builder.posterFactory.asyncPoster();
        assert !Objects.equals(syncPoster, asyncPoster);
    }

    /**
     * Async publish a event. If current thread is publishing event, this action will return.
     * <p>
     * 异步发布一个事件，当前线程正在有事件
     * 一般情况下, 事件总是被同步发布
     * 订阅者可能会进行异步执行
     * <p>
     * Post model:
     * 1. Same thread: the event only be posted at the caller thread
     * 2. Main thread: the event only be posted at the main thread
     * 3. Background thread: the event submit at background thread
     */
    public void submit(Event event) {
        if (event == null) {
            throw new NullPointerException();
        }
        // lock cache by access() method
        eventCacheAccessor.access();
        eventCacheAccessor.getCommander().cache(event);

        // flush cache to event queue
        if (!eventCacheAccessor.getQuerier().isFlushing()) {
            eventCacheAccessor.getCommander().flushTo(eventQueueAccessor);
        }
    }

    /**
     * Sync publish a event. If the sync poster is posting event, the current thread will blocking.
     *
     * @param event the event to publish
     * @throws InterruptedException the current be interrupted
     */
    public void publish(Event event) throws Exception {
        if (event == null) {
            throw new NullPointerException();
        }

        // get subscribers from cache
        // the first time to get form cache
        // can ensure needn't to filter with timestamp
        eventCacheAccessor.access();
        final Collection<Subscriber> cachedSubscribers = subscriberCacheAccessor.getQuerier().values();

        // the cachedSubscribers must be set to unmodifiable
        // prevent filter modify it
        final Collection<Subscriber> unmodifiableCachedSubscribers = Collections
                .unmodifiableCollection(cachedSubscribers);

        final Set<Subscriber> subscribers = new LinkedHashSet<>(
                subscriberFilter.doFilter(event, unmodifiableCachedSubscribers));

        // get subscribers of subscriberStore
        // must ensure subscriberStore isn't be modifying
        subscriberStoreAccessor.access();
        final Collection<Subscriber> storedSubscribers = subscriberStoreAccessor.getQuerier().subscribersOf(event);

        subscribers.addAll(storedSubscribers);

        final ReentrantLock postLock = this.postLock;
        postLock.lockInterruptibly();
        try {
            // use two poster to post event
            // ensure the state of poster not destroyed
            syncPoster.post(event, subscribers);
        } finally {
            postLock.unlock();
        }
    }

    /**
     * @see com.apehat.store.CacheCommander#flushTo(Store)
     * @see com.apehat.store.CacheCommander#flushTo(Accessor)
     */
    @Deprecated
    private void flush() {
    }

    // 异步进行事件的发送
    private void publish() throws InterruptedException {
        // 此处需要锁定事件队列与订阅者仓储，
        // 必须为可重入锁，防止事件队列与订阅者仓储是同一个实现

        // 必须先获取 publishLock， 一个线程防止锁队列后，未获取方法锁，造成死锁问题
        final ReentrantLock publishLock = this.publishLock;
        publishLock.lockInterruptibly();

        eventQueueAccessor.access();
        EventQueueQuerier querier = eventQueueAccessor.getQuerier();
        EventQueueCommander commander = eventQueueAccessor.getCommander();

        try {
            subscriberStoreAccessor.access();
            // 当每一次发布循环完成后，将进行信号的传输
            while (querier.hasNext()) {
                Event event;
                Collection<Subscriber> subscribers;
                event = commander.nextEvent();
                subscribers = subscriberStoreAccessor.getQuerier().subscribersOf(event);
                asyncPoster.post(event, subscribers);
            }
        } finally {
            publishLock.unlock();
        }
        // 发送信号
        // 也可以进行flush的调用
    }

    /**
     * 该方法是一个异步方法，所以在确保订阅者可以被用于订阅后，该方法将立即返回
     * <p>
     * Register a subscriber. The subscriber will be used to subscribe events with
     * {@code subscriber.subscribeTo()}.
     * <p>
     * Subscribe model:
     * 1. Subscribe events of current thread
     * 2. Subscribe global events
     * 3. Subscribe bus global event
     *
     * @throws RegisterException specified subscriber already be isRegistered to this
     */
    public void register(Subscriber subscriber) throws InterruptedException {
        if (subscriber == null) {
            throw new NullPointerException();
        }
        if (isRegistered(subscriber)) {
            throw new RegisterException(subscriber, " already registered.");
        }

        final ReentrantLock subscriberRegisterLock = this.registerLock;
        subscriberRegisterLock.lockInterruptibly();
        try {
            subscriberCacheAccessor.access();
            subscriberCacheAccessor.getCommander().cache(subscriber);
        } finally {
            subscriberRegisterLock.unlock();
        }
    }

    /**
     * Unregister specified subscriber.
     *
     * @param subscriber the subscriber to unregister
     */
    public void unregister(Subscriber subscriber) {
        if (subscriber == null) {
            throw new NullPointerException();
        }
        if (!isRegistered(subscriber)) {
            throw new IllegalArgumentException("Subscriber hadn't isRegistered.");
        }
        // 取消注册，可以使用标记，而不必先修改订阅者仓储
        // 这样可以使得取消订阅不必修改仓储
        // 可以选择合适的时机进行订阅仓储的修改
        // 此时可以通过创建一个订阅者包装器来实现
        // 但是该实现不应该由EventBus来负责，而是仓储的责任
    }

    private void markUp(Subscriber subscriber) {
    }

    private boolean isRegistered(Subscriber subscriber) {
        // 进行仓储与缓存的检查
        subscriberCacheAccessor.access();
        if (subscriberCacheAccessor.getQuerier().contains(subscriber)) {
            return true;
        } else {
            subscriberStoreAccessor.access();
            return subscriberStoreAccessor.getQuerier().contains(subscriber);
        }
    }

    public final static class Builder {

        private PosterFactory posterFactory;
        private CacheFactory<Event> eventCacheFactory;
        private StoreFactory<EventQueue> eventQueueFactory;
        private CacheFactory<Subscriber> subscriberCacheFactory;
        private StoreFactory<SubscriberStore> subscriberStoreFactory;

        public Builder setPosterFactory(PosterFactory posterFactory) {
            this.posterFactory = posterFactory;
            return this;
        }

        public Builder setEventCacheFactory(CacheFactory<Event> eventCacheFactory) {
            this.eventCacheFactory = eventCacheFactory;
            return this;
        }

        public Builder setEventQueueFactory(StoreFactory<EventQueue> eventQueueFactory) {
            this.eventQueueFactory = eventQueueFactory;
            return this;
        }

        public Builder setSubscriberCacheFactory(CacheFactory<Subscriber> subscriberCacheFactory) {
            this.subscriberCacheFactory = subscriberCacheFactory;
            return this;
        }

        public Builder setSubscriberStoreFactory(StoreFactory<SubscriberStore> subscriberStoreFactory) {
            this.subscriberStoreFactory = subscriberStoreFactory;
            return this;
        }

        public EventBus build() {
            if (posterFactory == null) {
                this.posterFactory = new DefaultPosterFactory();
            }
            return new EventBus(this);
        }
    }
}
