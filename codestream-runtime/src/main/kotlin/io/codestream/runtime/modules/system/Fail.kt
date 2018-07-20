package io.codestream.runtime.modules.system

import io.codestream.api.ComponentFailedException
import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.runtime.task.TaskDefContext

@Task(name = "fail", description = "Fails the current task execution with a message")
class Fail(
        @Parameter(description = "message to display")
        val message: String
) : SimpleTask {

    override fun run(ctx: RunContext) {
        throw ComponentFailedException(TaskDefContext.defn.id.stringId, message)
    }
}