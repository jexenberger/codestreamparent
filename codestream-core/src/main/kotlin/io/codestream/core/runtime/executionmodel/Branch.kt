package io.codestream.core.engine

abstract class Branch(override val id: String, parallel: Boolean = true) : Node {

    val children: List<Node> get() = _childList

    private val _childList: MutableList<Node> = mutableListOf()
    private var internalState:NodeState = NodeState.unrun

    override val state: NodeState
        get() = synchronized(this) { internalState }



    fun add(child: Node) = _childList.add(child)

    operator fun plusAssign(child: Node) {
        add(child)
    }

    operator fun plus(child: Node): Branch {
        add(child)
        return this
    }

    override fun nodeById(id: String): Node? {
        return super.nodeById(id)?.let { it } ?: run {
            var found:Node? = null
            val children = this.children.iterator()
            while (children.hasNext() && found == null) {
                found = children.next().nodeById(id)
            }
            return found
        }
    }

    override fun execute(ctx: ExecutionContext) : NodeState{
        internalState = NodeState.running
        try {
            var doAgain = true
            while (doAgain) {
                val pre = preTraversal(ctx)
                if (pre != BranchProcessingDirective.continueExecution) {
                    internalState = NodeState.completed
                }
                for (node in _childList) {
                    visitBeforeLeaf(node, ctx)
                    try {
                        val res = node.execute(ctx)
                        visitAfterLeaf(res, node, ctx)
                    } catch (e:Exception) {
                        visitWhenError(e, node, ctx)
                        throw e
                    }
                }
                val post = postTraversal(ctx)
                if (post != BranchProcessingDirective.again) {
                    doAgain = false
                }
            }
            internalState = NodeState.completed

        } catch (e:Exception) {
            internalState = NodeState.failed
            onError(e, ctx)
        }
        return internalState
    }

    abstract fun preTraversal(ctx: ExecutionContext): BranchProcessingDirective
    abstract fun postTraversal(ctx: ExecutionContext): BranchProcessingDirective
    abstract fun visitBeforeLeaf(leaf:Node, ctx: ExecutionContext)
    abstract fun visitWhenError(error:Exception, leaf:Node, ctx: ExecutionContext)
    abstract fun visitAfterLeaf(state:NodeState, leaf:Node, ctx: ExecutionContext)
    abstract fun onError(error:Exception, ctx: ExecutionContext)


}