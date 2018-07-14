package io.codestream.core.engine

class DefaultLeaf(id: String, val handler: (ctx:ExecutionContext) -> Unit) : Leaf(id) {

    private var internalState:NodeState = NodeState.unrun

    override val state: NodeState
        get() = synchronized(this) { internalState }

    override fun execute(ctx: ExecutionContext): NodeState {
        internalState = NodeState.running
        try {
            handler(ctx)
            internalState = NodeState.completed
        } catch (e:Exception) {
            internalState = NodeState.failed
            throw e
        }
        return internalState
    }
}