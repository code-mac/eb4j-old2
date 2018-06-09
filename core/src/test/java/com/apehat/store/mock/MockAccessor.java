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

package com.apehat.store.mock;

import com.apehat.store.Accessor;
import com.apehat.store.Querier;
import com.apehat.store.StoreCommander;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hanpengfei
 * @since 1.0
 */
public class MockAccessor implements Accessor {

    private final ReentrantLock connectLock = new ReentrantLock();

    private final StoreCommander storeCommander;
    private final Querier querier;

    public MockAccessor(StoreCommander storeCommander, Querier querier) {
        this.storeCommander = Objects.requireNonNull(storeCommander);
        this.querier = Objects.requireNonNull(querier);
    }

    //    public void access() {
    //        try {
    //            connectLock.lockInterruptibly();
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //    }

    @Override
    public Querier getQuerier() {
        return querier;
    }

    @Override
    public StoreCommander getCommander() {
        return storeCommander;
    }

    @Override
    public boolean isAccessable() {
        return connectLock.isLocked();
    }

    //    public void close() {
    //        connectLock.unlock();
    //    }
}
