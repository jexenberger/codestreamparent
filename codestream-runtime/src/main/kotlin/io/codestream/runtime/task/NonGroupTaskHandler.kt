package io.codestream.runtime.task

import io.codestream.api.FunctionalTask
import io.codestream.api.SimpleTask
import io.codestream.api.Task
import io.codestream.api.TaskId
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.StreamContext


class NonGroupTaskHandler(taskId: TaskId, taskDef: TaskDef) : BaseLeafTaskHandler(taskId, taskDef) {
    override fun run(ctx: StreamContext) {
        val task = ctx.get<Task>(taskId) ?: throw IllegalStateException("$taskId not resolved??!!")
        when (task) {
            is FunctionalTask<*> -> {
                val result = task.evaluate(ctx.bindings)
                if (taskDef is FunctionalTaskDef && taskDef.assign != null) {
                    ctx.bindings[taskDef.assign!!] = result
                }
            }
            else -> {
                if (taskDef is FunctionalTaskDef) {
                    throw IllegalStateException("$taskId has a definition for a task that does not return a result")
                }
                (task as SimpleTask).run(ctx.bindings)
            }
        }
    }


    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}