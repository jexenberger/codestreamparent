package io.codestream.core.engine

class DefaultBranch(id: String, parallel: Boolean,
                    val beforeLeafVisitor: ((leaf: Node, ctx: ExecutionContext) -> Unit)? = null,
                    val afterLeafVisitor: ((state: NodeState, leaf: Node, ctx: ExecutionContext) -> Unit)? = null,
                    val leafErrorVisitor: ((error:Exception, leaf: Node, ctx: ExecutionContext) -> Unit)? = null,
                    val preTraversal: ((ctx: ExecutionContext) -> BranchProcessingDirective)? = null,
                    val postTraversal: ((ctx: ExecutionContext) -> BranchProcessingDirective)? = null,
                    val onError: ((error: Exception, ctx: ExecutionContext) -> Unit)? = null
) : Branch(id, parallel) {

    override fun visitWhenError(error: Exception, leaf: Node, ctx: ExecutionContext) {
        this.leafErrorVisitor?.let { it(error, leaf, ctx) }
    }

    override fun preTraversal(ctx: ExecutionContext): BranchProcessingDirective {
        return this.preTraversal?.let { it(ctx) } ?: BranchProcessingDirective.continueExecution
    }

    override fun postTraversal(ctx: ExecutionContext): BranchProcessingDirective {
        return this.postTraversal?.let { it(ctx) } ?: BranchProcessingDirective.done
    }

    override fun visitBeforeLeaf(leaf: Node, ctx: ExecutionContext) {
        this.beforeLeafVisitor?.let { it(leaf, ctx) }
    }

    override fun visitAfterLeaf(state: NodeState, leaf: Node, ctx: ExecutionContext) {
        this.afterLeafVisitor?.let { it(state, leaf, ctx) }
    }

    override fun onError(error: Exception, ctx: ExecutionContext) {
        this.onError?.let { it(error, ctx) }
    }
}