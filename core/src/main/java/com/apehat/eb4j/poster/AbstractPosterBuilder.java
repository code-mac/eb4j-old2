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

/**
 * @author hanpengfei
 * @since 1.0
 */
@Deprecated
public abstract class AbstractPosterBuilder implements PosterBuilder {

    private final ExceptionHandlerChain chain;

    public AbstractPosterBuilder() {
        this.chain = new ExceptionHandlerChain(null);
    }

    @Override
    public PosterBuilder addExceptionHandler(ExceptionHandler exceptionHandler) {
        chain.addHandler(exceptionHandler);
        return this;
    }

    // TODO 此处没有达到不可修改的目的
    @Deprecated
    ExceptionHandlerChain exceptionHandlerChain() {
        //        return ExceptionHandlerChain.unmodifiableChain(chain);
        return chain;
    }
}
