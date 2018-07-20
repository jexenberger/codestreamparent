package io.codestream.runtime

import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.TaskId
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.runtime.task.NonGroupTaskHandler
import io.codestream.runtime.task.TaskDefContext

class SimpleCompositeTask(taskId: TaskId,
                          descriptor: TaskDescriptor,
                          nestedContext: io.codestream.runtime.StreamContext,
                          errorTask: NonGroupTaskHandler? = null,
                          finallyTask: NonGroupTaskHandler? = null) : CompositeTask(taskId, descriptor, nestedContext, errorTask, finallyTask), SimpleTask{

    override fun run(ctx: RunContext) {
        val taskDef = TaskDefContext.defn
        executeCompositeTask(ctx, taskDef)

    }

}