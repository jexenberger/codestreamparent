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
            visitWhenError(e, this, ctx);
            internalState = NodeState.failed
            throw e
        }
        return internalState
    }

    abstract fun visitBeforeLeaf(leaf: Node<T>, ctx: T) : Boolean
    abstract fun visitWhenError(error: Exception, leaf: Node<T>, ctx: T)
    abstract fun visitAfterLeaf(state: NodeState, leaf: Node<T>, ctx: T, timeTaken: Pair<Long, Unit>)
    abstract fun visitSkipped(state: NodeState, leaf: Node<T>, ctx: T)
    abstract fun handle(ctx:T)


}