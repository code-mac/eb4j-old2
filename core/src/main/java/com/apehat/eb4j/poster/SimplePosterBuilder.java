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

package com.apehat.eb4j.poster;

import com.apehat.eb4j.event.Event;
import com.apehat.eb4j.subscription.Subscriber;

/**
 * @author hanpengfei
 * @since 1.0
 */
@Deprecated
public final class SimplePosterBuilder extends AbstractPosterBuilder {

    @Override
    public Poster build() {
        return new AbstractPoster(this) {
            @Override
            public void doPublish(Event event, Subscriber subscriber) {
                subscriber.onEvent(event);
            }
        };
    }
}
