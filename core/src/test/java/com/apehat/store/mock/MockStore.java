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

package com.apehat.store.mock;

import com.apehat.store.Accessor;
import com.apehat.store.Querier;
import com.apehat.store.Store;
import com.apehat.store.StoreCommander;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class MockStore implements Store {

    private final Accessor accessor;
    private final Collection<Object> collation;
    private final String accessToken;

    public MockStore(String accessToken) {
        this.accessToken = Objects.requireNonNull(accessToken);
        this.collation = new LinkedList<>();
        StoreCommander storeCommander = new MockStoreCommander();
        Querier querier = new MockQuerier();
        this.accessor = new MockAccessor(storeCommander, querier);
    }

    @Override
    public synchronized Accessor getAccessor(String accessToken) throws IllegalAccessException {
        if (!Objects.equals(this.accessToken, accessToken)) {
            throw new IllegalAccessException();
        }
        return accessor;
    }

    class MockStoreCommander implements StoreCommander {

        @Override
        public void store(Object o) {
            collation.add(o);
        }

        @Override
        public void remove(Object value) {

        }
    }

    class MockQuerier implements Querier {
        @Override
        public boolean contains(Object value) {
            return false;
        }

        @Override
        public int size() {
            return collation.size();
        }

        @Override
        public Collection values() {
            return null;
        }
    }
}
