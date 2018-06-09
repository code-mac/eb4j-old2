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

import com.apehat.store.mock.MockStore;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class StoreTest {

    private static final String ACCESS_TOKEN = UUID.randomUUID().toString();
    private final Store<?> store = new MockStore(ACCESS_TOKEN);

    @Test
    public void testGetConnection() throws IllegalAccessException {
        Accessor accessor = store.getAccessor(ACCESS_TOKEN);
        assert accessor != null;
    }

    @Test(expectedExceptions = IllegalAccessException.class)
    public void testGetConnectionWithInvaildToken() throws IllegalAccessException {
        store.getAccessor(UUID.randomUUID().toString());
    }
}
