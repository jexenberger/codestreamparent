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