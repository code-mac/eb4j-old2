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

package com.apehat.eb4j.subscription;

import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.event.EventType;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hanpengfei
 * @since 1.0
 */
public final class EventTypeFilter implements SubscriberFilter {

    @Override
    public Collection<Subscriber> doFilter(Event event, Collection<Subscriber> subscribers) {
        Set<Subscriber> filteredSubscribers = new LinkedHashSet<>();
        final EventType eventType = event.head().type();
        for (Subscriber subscriber : subscribers) {
            EventType subscribeTo = subscriber.subscribeTo();
            if (subscribeTo.belongTo(eventType)) {
                filteredSubscribers.add(subscriber);
            }
        }
        return filteredSubscribers;
    }
}
