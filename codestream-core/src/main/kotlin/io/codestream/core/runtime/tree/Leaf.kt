package io.codestream.core.runtime.tree

abstract class Leaf<T>(override val id: String) : Node<T> {

    abstract fun visitBeforeLeaf(leaf: Node<T>, ctx: T)
    abstract fun visitWhenError(error: Exception, leaf: Node<T>, ctx: T)
    abstract fun visitAfterLeaf(state: NodeState, leaf: Node<T>, ctx: T)


}