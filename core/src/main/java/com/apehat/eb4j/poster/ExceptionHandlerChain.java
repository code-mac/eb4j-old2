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

import com.apehat.logging.Level;
import com.apehat.logging.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hanpengfei
 * @since 1.0
 */
class ExceptionHandlerChain implements ExceptionHandler {

    private final Set<ExceptionHandler> handlers;
    private final Logger logger;

    protected ExceptionHandlerChain(Logger logger) {
        this.handlers = new LinkedHashSet<>();
        this.logger = logger;
    }

    @Override
    public void handle(SubscriptionException e) {
        for (ExceptionHandler handler : handlers) {
            try {
                handler.handle(e);
            } catch (Exception e1) {
                logger.log(Level.EXCEPTION, e1.getMessage());
            }
        }
    }

    void addHandler(ExceptionHandler handler) {
        handlers.add(handler);
    }

    public int length() {
        return handlers.size();
    }
}
