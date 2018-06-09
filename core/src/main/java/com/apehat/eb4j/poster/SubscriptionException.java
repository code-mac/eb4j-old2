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

/**
 * @author hanpengfei
 * @since 1.0
 */
public class SubscriptionException extends RuntimeException {

    private final String dispatcherId;
    private final long occurredOn = System.currentTimeMillis();
    private final Subscriber subscriber;
    private final Event event;
    private final Exception exception;

    public SubscriptionException(String dispatcherId, Subscriber subscriber, Event event, Exception exception) {
        super(exception);
        this.dispatcherId = dispatcherId;
        this.subscriber = subscriber;
        this.event = event;
        this.exception = exception;
    }

    public String getDispatcherId() {
        return dispatcherId;
    }

    public long getOccurredOn() {
        return occurredOn;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public Exception getException() {
        return exception;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "Poster " + dispatcherId + " publish " + event + " to " + subscriber + " throw exception on " + occurredOn + "\n" + exception;
    }
}
