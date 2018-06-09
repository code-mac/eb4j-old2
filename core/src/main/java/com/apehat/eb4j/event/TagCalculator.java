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
 * Generate a tag by specified event.
 *
 * @author hanpengfei
 * @since 1.0
 */
// TODO 这里封装了一套事件TAG的生成算法，该算法生成的事件应与事件紧密相关，并可以在以后的验证系统中使用
// 因为不能保证外部提供的事件体是一个值对象
public interface TagCalculator {}
