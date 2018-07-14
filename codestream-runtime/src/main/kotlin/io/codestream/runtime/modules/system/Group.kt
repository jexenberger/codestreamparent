package io.codestream.runtime.modules.system

import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Task
import io.codestream.api.Directive

@Task(name = "group", description = "Groups a set of related tasks together, allows the tasks to be executed conditionally or in parallel")
class Group : GroupTask {
    override fun before(ctx: RunContext) = Directive.continueExecution

    override fun after(ctx: RunContext) = Directive.done

    override fun onFinally(ctx: RunContext) {
    }

}