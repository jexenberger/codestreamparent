package io.codestream.runtime.tree

class DefaultBranch<T>(id: String, parallel: Boolean,

                    val preTraversal: ((ctx: T) -> BranchProcessingDirective)? = null,
                    val postTraversal: ((ctx: T) -> BranchProcessingDirective)? = null,
                    val onError: ((error: Exception, ctx: T) -> Unit)? = null) : Branch<T>(id, parallel) {

    override fun enterBranch(ctx: T) {
    }

    override fun exitBranch(ctx: T) {
    }


    override fun preTraversal(ctx: T): BranchProcessingDirective {
        return this.preTraversal?.let { it(ctx) } ?: BranchProcessingDirective.continueExecution
    }

    override fun postTraversal(ctx: T): BranchProcessingDirective {
        return this.postTraversal?.let { it(ctx) } ?: BranchProcessingDirective.done
    }

    override fun onError(error: Exception, ctx: T) {
        this.onError?.let { it(error, ctx) }
    }
}