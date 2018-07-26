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