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

/**
 * @author hanpengfei
 * @since 1.0
 */
@Deprecated
public class PosterDescriptor implements Poster.Descriptor {

    private final String id;
    private final State state;
    private final int publishedCount;

    public PosterDescriptor(String id, State state, int publishedCount) {
        this.id = id;
        this.state = state;
        this.publishedCount = publishedCount;
    }

    public String id() {
        return id;
    }

    public State state() {
        return state;
    }

    @Override
    public long postedCount() {
        return 0;
    }

    public int publishedCount() {
        return publishedCount;
    }

    @Override
    public String toString() {
        return "Poster{" + "id='" + id + '\'' + ", state=" + state + ", publishedCount=" + publishedCount + '}';
    }
}
