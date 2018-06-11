package io.codestream.core.runtime.tree

import io.codestream.util.returnIfTrue

interface
Node<T> {

    val id: String
    val state: NodeState get() = NodeState.unrun

    fun nodeById(id: String) = this.id.equals(id).returnIfTrue { this }

    fun execute(ctx: T): NodeState


}