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

package com.apehat.eb4j.mock;

import com.apehat.eb4j.Callback;
import com.apehat.eb4j.ClassEventType;
import com.apehat.eb4j.EventBusTest;
import com.apehat.eb4j.event.EventType;
import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.subscription.Subscriber;

import java.util.Objects;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class MockSubscriber implements Subscriber {

    private final Callback callback;

    public MockSubscriber(Callback callback) {
        this.callback = Objects.requireNonNull(callback);
    }

    @Override
    public EventType subscribeTo() {
        return new ClassEventType(EventBusTest.class);
    }

    @Override
    public void onEvent(Event event) {
        callback.call(event);
    }
}
