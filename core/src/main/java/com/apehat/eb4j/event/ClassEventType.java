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

import java.util.Objects;

/**
 * @author hanpengfei
 * @since 1.0
 */
public final class ClassEventType implements EventType {

    private final Class<?> cls;

    public ClassEventType(Class<?> cls) {
        this.cls = Objects.requireNonNull(cls);
    }

    @Override
    public boolean belongTo(EventType eventType) {
        if (eventType == null) {
            return false;
        }
        if (!(eventType instanceof ClassEventType)) {
            return false;
        }
        ClassEventType aType = (ClassEventType) eventType;
        final Class<?> clazz = aType.cls;
        return cls.equals(clazz) || clazz.isAssignableFrom(cls);
    }
}
