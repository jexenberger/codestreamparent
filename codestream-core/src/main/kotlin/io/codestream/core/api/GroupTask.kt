package io.codestream.core.api

import io.codestream.core.runtime.tree.BranchProcessingDirective

interface GroupTask : Task {

    fun before(ctx: RunContext): BranchProcessingDirective

    fun after(ctx: RunContext): BranchProcessingDirective

    fun onError(error:TaskError, ctx:RunContext) {
        throw error.exception
    }

    fun onFinally(ctx: RunContext)

}