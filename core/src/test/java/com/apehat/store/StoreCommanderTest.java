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

package com.apehat.store;

import com.apehat.store.mock.MockStore;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class StoreCommanderTest {

    private static final String ACCESS_TOKEN = UUID.randomUUID().toString();
    private final Store<?> store = new MockStore(ACCESS_TOKEN);

    @Test
    public void testAdd() throws Exception {
        Accessor<?> accessor = store.access(ACCESS_TOKEN);
        try (StoreCommander commander = accessor.getCommander()) {
            commander.store("1");
        }
        try (Querier querier = accessor.getQuerier()) {
            int size = querier.size();
            assertEquals(1, size);
        }
    }
}