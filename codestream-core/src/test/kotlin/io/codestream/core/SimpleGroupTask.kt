package io.codestream.core

import io.codestream.core.api.GroupTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.TaskError
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
import io.codestream.core.api.annotations.TaskContext
import io.codestream.core.runtime.tree.BranchProcessingDirective

@Task("group", "A group task which does stuff")
class SimpleGroupTask(
        @Parameter(description = "description", default = "test") val value: String,
        @TaskContext val context:SimpleGroupTaskContext
) : GroupTask {

    override fun before(ctx: RunContext): BranchProcessingDirective {
        context.before = true
        return BranchProcessingDirective.continueExecution
    }

    override fun after(ctx: RunContext): BranchProcessingDirective {
        context.after = true
        return BranchProcessingDirective.done
    }

    override fun onError(error: TaskError, ctx: RunContext) {
        context.error = true
    }

    override fun onFinally(ctx: RunContext) {
        context.onFinally = true
    }


}