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

package com.apehat.eb4j.event;

/**
 * The head of event. Contains the occur time and type and tag.
 *
 * @author hanpengfei
 * @since 1.0
 */
public final class EventHead {

    private final long occurredOn;
    private final EventType type;
    private final String tag;

    public EventHead(EventType type) {
        this.occurredOn = System.currentTimeMillis();
        this.type = type;
        this.tag = calculateTag();
    }

    /**
     * Returns a long value as the occur timestamp of event. This value is a immutable value,
     * that is to say, tis value should only generate at an event be constructed, and shouldn't be
     * changed with any reason.
     *
     * @return the occur timestamp of the event.
     */
    public long occurredOn() {
        return occurredOn;
    }

    /**
     * Returns the type of the event.
     *
     * @return the type of the event.
     */
    public EventType type() {
        return type;
    }

    /**
     * Returns the tag of event. By the tag, can always track and identity event.
     *
     * @return the tag of event.
     */
    public String tag() {
        return tag;
    }

    private String calculateTag() {
        return null;
    }
}
