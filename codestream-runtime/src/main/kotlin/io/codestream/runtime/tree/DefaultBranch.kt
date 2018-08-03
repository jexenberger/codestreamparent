/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.runtime.tree

import io.codestream.api.Directive
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DefaultBranch<T>(id: String, parallel: Boolean,

                       val preTraversal: ((ctx: T) -> Directive)? = null,
                       val postTraversal: ((ctx: T) -> Directive)? = null,
                       val onError: ((error: Exception, ctx: T) -> Unit)? = null) : Branch<T>(id, parallel) {

    override fun enterBranch(ctx: T) {
    }

    override fun exitBranch(ctx: T) {
    }


    override fun preTraversal(ctx: T): Directive {
        return this.preTraversal?.let { it(ctx) } ?: Directive.continueExecution
    }

    override fun postTraversal(ctx: T): Directive {
        return this.postTraversal?.let { it(ctx) } ?: Directive.done
    }

    override fun onError(error: Exception, ctx: T) {
        this.onError?.let { it(error, ctx) }
    }


}