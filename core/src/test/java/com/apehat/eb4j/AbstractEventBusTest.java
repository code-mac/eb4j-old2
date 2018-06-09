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

import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.mock.MockEvent;
import com.apehat.eb4j.mock.MockSubscriber;

/**
 * @author hanpengfei
 * @since 1.0
 */
public abstract class AbstractEventBusTest {

    private static final EventBus EVENT_BUS = new EventBus.Builder().build();

    public AbstractEventBusTest() {}

    protected static void registerSubscriber(Callback callback) {
        registerSubscriber(EVENT_BUS, callback);
    }

    protected static void registerSubscriber(EventBus eventBus, Callback callback) {
        try {
            eventBus.register(new MockSubscriber(callback));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected EventBus getEventBusApparatus() {
        return EVENT_BUS;
    }

    protected EventBus newEventBusApparatus() {
        return new EventBus.Builder().build();
    }

    protected Event newEventApparatus() {
        return new MockEvent();
    }
}
