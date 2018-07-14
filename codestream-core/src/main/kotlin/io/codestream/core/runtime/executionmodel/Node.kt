package io.codestream.core.engine

import io.codestream.util.returnIfTrue

interface Node {

    val id: String
    val state: NodeState get() = NodeState.unrun

    fun nodeById(id: String) = this.id.equals(id).returnIfTrue { this }

    fun execute(ctx: ExecutionContext): NodeState


}