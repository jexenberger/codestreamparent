package io.codestream.runtime.modules.system

import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Task
import io.codestream.runtime.tree.BranchProcessingDirective

@Task(name = "group", description = "Groups a set of related tasks together, allows the tasks to be executed conditionally or in parallel")
class Group : GroupTask {
    override fun before(ctx: RunContext) = BranchProcessingDirective.continueExecution

    override fun after(ctx: RunContext) = BranchProcessingDirective.done

    override fun onFinally(ctx: RunContext) {
    }

}