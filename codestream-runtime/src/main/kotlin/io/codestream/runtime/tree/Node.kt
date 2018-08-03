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

import io.codestream.util.ifTrue

interface Node<T> {

    val id: String
    val state: NodeState get() = NodeState.unrun

    fun nodeById(id: String) = this.id.equals(id).ifTrue { this }

    fun execute(ctx: T): NodeState

    fun dump(level:Int = 0) {
        for (i in 0..level) {
            print("  ")
        }
        println("-> $id")
    }

    fun traverse(handler: (Node<T>, Node<T>?) -> Unit) {
        handler(this, null)
    }

}