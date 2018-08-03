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

import io.codestream.util.Timer

abstract class Leaf<T>(override val id: String) : Node<T> {

    @set:Synchronized
    protected var internalState: NodeState = NodeState.unrun

    override val state: NodeState get() = internalState

    override fun execute(ctx: T): NodeState {
        internalState = NodeState.running
        try {
            if (visitBeforeLeaf(this, ctx)) {
                val timeTaken = Timer.run {  handle(ctx) }
                internalState = NodeState.completed
                visitAfterLeaf(internalState, this, ctx, timeTaken)
            } else {
                internalState = NodeState.skipped
                visitSkipped(internalState, this, ctx)
            }
        } catch (e: Exception) {
            val errorToThrow = visitWhenError(e, this, ctx)
            internalState = NodeState.failed
            throw errorToThrow
        }
        return internalState
    }

    abstract fun visitBeforeLeaf(leaf: Node<T>, ctx: T) : Boolean
    abstract fun visitWhenError(error: Exception, leaf: Node<T>, ctx: T) : Exception
    abstract fun visitAfterLeaf(state: NodeState, leaf: Node<T>, ctx: T, timeTaken: Pair<Long, Unit>)
    abstract fun visitSkipped(state: NodeState, leaf: Node<T>, ctx: T)
    abstract fun handle(ctx:T)


}