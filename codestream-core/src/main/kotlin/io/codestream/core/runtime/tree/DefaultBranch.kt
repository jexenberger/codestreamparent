package io.codestream.core.runtime.tree

import io.codestream.core.api.TaskError

class DefaultBranch<T>(id: String, parallel: Boolean,

                    val preTraversal: ((ctx: T) -> BranchProcessingDirective)? = null,
                    val postTraversal: ((ctx: T) -> BranchProcessingDirective)? = null,
                    val onError: ((error: TaskError, ctx: T) -> Unit)? = null) : Branch<T>(id, parallel) {

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

    override fun onError(error: TaskError, ctx: T) {
        this.onError?.let { it(error, ctx) }
    }
}