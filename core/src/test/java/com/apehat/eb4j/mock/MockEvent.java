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

import com.apehat.eb4j.EventBusTest;
import com.apehat.eb4j.event.ClassEventType;
import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.event.EventHead;
import com.apehat.eb4j.source.SourceId;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class MockEvent implements Event {
    private final EventHead head;
    private final SourceId sourceId;

    public MockEvent() {
        this.head = new EventHead(new ClassEventType(EventBusTest.class));
        this.sourceId = new SourceId() {
            @Override
            public String toString() {
                return EventBusTest.class.getName();
            }
        };
    }

    @Override
    public EventHead head() {
        return this.head;
    }

    @Override
    public Object body() {
        return "empty event";
    }

    @Override
    public SourceId source() {
        return this.sourceId;
    }
}
