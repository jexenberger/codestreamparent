package io.codestream.api


interface GroupTask : Task {

    fun before(ctx: RunContext): Directive

    fun after(ctx: RunContext): Directive

    fun onError(error: TaskError, ctx: RunContext) {
        throw error.exception
    }

    fun onFinally(ctx: RunContext)

}