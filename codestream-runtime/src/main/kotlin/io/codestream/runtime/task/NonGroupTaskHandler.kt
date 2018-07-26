package io.codestream.runtime.task

import io.codestream.api.*
import io.codestream.api.events.TaskErrorEvent
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.StreamContext
import io.codestream.runtime.tree.Node


class NonGroupTaskHandler(taskId: TaskId, taskDef: TaskDef) : BaseLeafTaskHandler(taskId, taskDef) {
    override fun run(ctx: StreamContext) {
        val task = try {
             ctx.get<Task>(taskId) ?: throw IllegalStateException("$taskId not resolved??!!")
        } catch (e:IllegalArgumentException) {
            throw ComponentFailedException(taskId.id, "Unable to create instance -> '${e}'")
        }
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

    override fun visitWhenError(error: Exception, leaf: Node<StreamContext>, ctx: StreamContext): Exception {
        val taskError = TaskError.bubble(taskId, error, ctx.bindings)
        ctx.events.publish(TaskErrorEvent(taskId, taskError))
        return taskError
    }

    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}