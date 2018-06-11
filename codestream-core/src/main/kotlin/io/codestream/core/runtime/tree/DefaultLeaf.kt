package io.codestream.core.runtime.tree

open class DefaultLeaf<T>(id: String,
                       val beforeLeafVisitor: ((leaf: Node<T>, ctx: T) -> Unit)? = null,
                       val afterLeafVisitor: ((state: NodeState, leaf: Node<T>, ctx: T) -> Unit)? = null,
                       val leafErrorVisitor: ((error:Exception, leaf: Node<T>, ctx: T) -> Unit)? = null,
                       val handler: (ctx: T) -> Unit) : Leaf<T>(id) {

    constructor(id:String,handler: (ctx: T) -> Unit  )
            : this(id, null, null, null, handler)

    private var internalState: NodeState = NodeState.unrun

    override val state: NodeState
        get() = synchronized(this) { internalState }

    override fun execute(ctx: T): NodeState {
        internalState = NodeState.running
        try {
            visitBeforeLeaf(this, ctx)
            handler(ctx)
            internalState = NodeState.completed
            visitAfterLeaf(internalState, this, ctx)
        } catch (e:Exception) {
            visitWhenError(e, this, ctx);
            internalState = NodeState.failed
            throw e
        }
        return internalState
    }

    override fun visitWhenError(error: Exception, leaf: Node<T>, ctx: T) {
        this.leafErrorVisitor?.let { it(error, leaf, ctx) }
    }


    override fun visitBeforeLeaf(leaf: Node<T>, ctx: T) {
        this.beforeLeafVisitor?.let { it(leaf, ctx) }
    }

    override fun visitAfterLeaf(state: NodeState, leaf: Node<T>, ctx: T) {
        this.afterLeafVisitor?.let { it(state, leaf, ctx) }
    }


}