package io.codestream.runtime.tree

import io.codestream.api.Directive

class DefaultBranch<T>(id: String, parallel: Boolean,

                       val preTraversal: ((ctx: T) -> Directive)? = null,
                       val postTraversal: ((ctx: T) -> Directive)? = null,
                       val onError: ((error: Exception, ctx: T) -> Unit)? = null) : Branch<T>(id, parallel) {

    override fun enterBranch(ctx: T) {
    }

    override fun exitBranch(ctx: T) {
    }


    override fun preTraversal(ctx: T): Directive {
        return this.preTraversal?.let { it(ctx) } ?: Directive.continueExecution
    }

    override fun postTraversal(ctx: T): Directive {
        return this.postTraversal?.let { it(ctx) } ?: Directive.done
    }

    override fun onError(error: Exception, ctx: T) {
        this.onError?.let { it(error, ctx) }
    }
}