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

package com.apehat.eb4j.event;

import com.apehat.eb4j.source.EventSource;
import com.apehat.lang.annotation.NotNull;

/**
 * 事件应该是一个不可变的对象
 * 但是，现在无法保证的是，事件体不会发生改变。
 * 包括在事件发布后，发布者不会对事件进行改变。以及订阅者不会对事件进行修改。
 * <p>
 * 在这种情况下，想要保证事件的不变性，只能对事件进行深度的拷贝。
 * 但是，在这种情况下，事件的深度拷贝必定带来性能问题。尤其是面对比较庞大的事件时，对CPU以及内存存储的消耗必定是巨大的
 *
 * @author hanpengfei
 * @since 1.0
 */
public interface Event {

    @NotNull
    EventHead head();

    @NotNull
    Object body();

    @NotNull
    EventSource source();
}
