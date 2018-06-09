///*
// * Copyright Apehat.com
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.apehat.eb4j;
//
//import com.apehat.eb4j.event.Event;
//import com.apehat.eb4j.event.EventQueue;
//import com.apehat.eb4j.event.EventType;
//import com.apehat.eb4j.subscription.Subscriber;
//import com.apehat.eb4j.subscription.SubscriberStore;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * @author hanpengfei
// * @since 1.0
// */
//@Deprecated
//public class MemSubmitStore implements EventQueue, SubscriberStore {
//
//    private final Set<Integer> subscribersHashTable = new CopyOnWriteArraySet<>();
//
//    private final Queue<Wrapper<?>> queue = new LinkedBlockingQueue<>();
//
//    private final Map<EventType, Set<Subscriber>> subscribersMap = new ConcurrentHashMap<>();
//    private volatile Event preEvent;
//
//    @Override
//    public void store(Subscriber subscriber) {
//        if (contains(subscriber)) {
//            throw new RuntimeException("Subscriber " + subscriber + " already exists.");
//        }
//        subscribersHashTable.add(subscriber.hashCode());
//        queue.offer(new Wrapper<>(subscriber));
//    }
//
//    private int subscriberIdentity(Subscriber subscriber) {
//        return subscriber.hashCode();
//    }
//
//    @Override
//    public void remove(Subscriber subscriber) {
//        if (contains(subscriber)) {
//            Set<Subscriber> subscribers = subscribersMap.get(subscriber.subscribeTo());
//            if (!subscribers.remove(subscriber)) {
//                queue.remove(new Wrapper<>(subscriber));
//            }
//            subscribersHashTable.remove(subscriberIdentity(subscriber));
//        }
//    }
//
//    @Override
//    public Collection<Subscriber> subscribersOf(Event event) {
//        flush();
//        Set<Subscriber> subscribers = new LinkedHashSet<>();
//        Set<EventType> eventTypes = subscribersMap.keySet();
//        for (EventType type : eventTypes) {
//            if (type.belongTo(event.head().type())) {
//                subscribers.addAll(subscribersMap.get(type));
//            }
//        }
//        return subscribers;
//    }
//
//    public Iterable<Subscriber> subscribers() {
//        return subscribersOf(preEvent);
//    }
//
//    @Override
//    public int size() {
//        return subscribersHashTable.size();
//    }
//
//    @Override
//    public void put(Event event) {
//        queue.offer(new Wrapper<>(event));
//    }
//
//    // TODO  此处有副作用
//    @Override
//    public Event nextEvent() {
//        flush();
//        Wrapper<?> wrapper = queue.poll();
//        if (wrapper != null) {
//            assert wrapper.isEvent();
//            Event event = (Event) wrapper.source;
//            preEvent = event;
//            return event;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean hasNext() {
//        flush();
//        return queue.peek() != null;
//    }
//
//    private void flush() {
//        Wrapper<?> peek = queue.peek();
//        while (peek != null && !peek.isEvent()) {
//            Wrapper<?> poll = queue.poll();
//            assert peek.equals(poll);
//            Subscriber subscriber = (Subscriber) poll.source;
//            EventType kind = subscriber.subscribeTo();
//            Set<Subscriber> subscribers = subscribersMap.get(kind);
//            if (subscribers == null) {
//                subscribers = new CopyOnWriteArraySet<>();
//                subscribersMap.put(kind, subscribers);
//            }
//            subscribers.add(subscriber);
//            peek = queue.peek();
//        }
//    }
//
//    public boolean contains(Subscriber subscriber) {
//        return subscribersHashTable.contains(subscriberIdentity(subscriber));
//    }
//
//    private class Wrapper<T> {
//
//        private final T source;
//        private final boolean isEvent;
//
//        private Wrapper(T source) {
//            this.source = Objects.requireNonNull(source);
//            isEvent = source instanceof Event;
//        }
//
//        boolean isEvent() {
//            return isEvent;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            }
//            if (o == null || getClass() != o.getClass()) {
//                return false;
//            }
//            Wrapper<?> wrapper = (Wrapper<?>) o;
//            return isEvent == wrapper.isEvent && Objects.equals(source, wrapper.source);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(source, isEvent);
//        }
//    }
//}
