package io.codestream.runtime

import io.codestream.api.Directive
import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.TaskError
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.annotations.TaskContext

@Task("group", "A group task which does stuff")
class SimpleGroupTask(
        @Parameter(description = "description", default = "test") val value: String,
        @TaskContext val context: SimpleGroupTaskContext
) : GroupTask {

    override fun before(ctx: RunContext): Directive {
        context.before = true
        return Directive.continueExecution
    }

    override fun after(ctx: RunContext): Directive {
        context.after = true
        return Directive.done
    }

    override fun onError(error: TaskError, ctx: RunContext) {
        context.error = true
    }

    override fun onFinally(ctx: RunContext) {
        context.onFinally = true
    }


}