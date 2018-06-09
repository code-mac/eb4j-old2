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

package com.apehat.store;

/**
 * 可以通过该模式建立一套认真框架
 *
 * @author hanpengfei
 * @since 1.0
 */
public interface Store<T> {

    /**
     * Returns the accessor of the current store.
     *
     * @param accessToken the access token, be used to access store.
     * @return the accessor of the current store
     * @throws IllegalAccessException the token is invalid
     */
    Accessor<T> getAccessor(String accessToken) throws IllegalAccessException;
}
