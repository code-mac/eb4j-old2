/*
 * Copyright (c) 2018 Apehat.com
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

package com.apehat.eb4j.poster;

import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.subscription.Subscriber;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hanpengfei
 * @since 1.0
 */
@Deprecated
public abstract class AbstractPoster implements Poster {

    private final String id;
    private final ExceptionHandlerChain exceptionHandlerChain;

    private volatile AtomicInteger publishedCount;

    private volatile State state;

    protected AbstractPoster(AbstractPosterBuilder builder) {
        this.publishedCount = new AtomicInteger();
        this.state = State.WAITING;
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.exceptionHandlerChain = builder.exceptionHandlerChain();
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final void post(Event event, Iterable<Subscriber> subscribers) {
        try {
            makePublishing();
            for (Subscriber subscriber : subscribers) {
                try {
                    doPublish(event, subscriber);
                } catch (Exception e) {
                    exceptionHandlerChain.handle(new SubscriptionException(id, subscriber, event, e));
                }
            }
        } finally {
            publishedCount.incrementAndGet();
            //            ++publishedCount;
            makeWaiting();
        }
    }

    protected abstract void doPublish(Event event, Subscriber subscriber);

    @Override
    public final State state() {
        return state;
    }

    private void makePublishing() {
        state = State.PUBLISHING;
    }

    private void makeWaiting() {
        state = State.WAITING;
    }
}
