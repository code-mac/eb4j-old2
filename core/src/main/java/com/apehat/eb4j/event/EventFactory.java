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
 * @author hanpengfei
 * @since 1.0
 */
public class EventFactory {

    private static boolean initialized = false;
    // 该生成器只能被设置一次
    private static TagCalculator tagCalculator = null;

    private EventFactory() {
        if (initialized) {
            throw new AssertionError("Instance of " + EventFactory.class + " already instantiated.");
        }
        initialized = true;
    }

    public static EventFactory getInstance() {
        return Instance.FACTORY;
    }

    // todo 确定事件构造需要的基本属性

    // new event 时 提供 tagCalculator 时，使用提供的计算器进行计算

    // 不提供计算器时，使用默认的计算器进行计算

    public static TagCalculator getTagCalculator() {
        return tagCalculator;
    }

    private static class Instance {
        private static final EventFactory FACTORY = new EventFactory();
    }
}
