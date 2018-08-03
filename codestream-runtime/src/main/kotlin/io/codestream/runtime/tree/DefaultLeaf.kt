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

open class DefaultLeaf<T>(id: String,
                          val beforeLeafVisitor: ((leaf: Node<T>, ctx: T) -> Boolean)? = null,
                          val skippedVisitor: ((leaf: Node<T>, ctx: T) -> Unit)? = null,
                          val afterLeafVisitor: ((state: NodeState, leaf: Node<T>, ctx: T) -> Unit)? = null,
                          val leafErrorVisitor: ((error: Exception, leaf: Node<T>, ctx: T) -> Unit)? = null,
                          val handler: (ctx: T) -> Unit) : Leaf<T>(id) {

    constructor(id: String, handler: (ctx: T) -> Unit)
            : this(id, null, null, null, null, handler)

    override fun visitSkipped(state: NodeState, leaf: Node<T>, ctx: T) {
        this.skippedVisitor?.let { it(leaf, ctx) }
    }


    override fun visitWhenError(error: Exception, leaf: Node<T>, ctx: T): Exception {
        this.leafErrorVisitor?.let { it(error, leaf, ctx) }
        return error
    }


    override fun visitBeforeLeaf(leaf: Node<T>, ctx: T): Boolean {
        return this.beforeLeafVisitor?.let { it(leaf, ctx) } ?: true
    }

    override fun visitAfterLeaf(state: NodeState, leaf: Node<T>, ctx: T, timeTaken: Pair<Long, Unit>) {
        this.afterLeafVisitor?.let { it(state, leaf, ctx) }
    }

    override fun handle(ctx: T) {
        handler(ctx)
    }
}