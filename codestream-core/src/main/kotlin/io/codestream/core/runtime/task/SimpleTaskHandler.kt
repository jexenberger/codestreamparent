package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.TaskError
import io.codestream.core.api.metamodel.TaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.api.TaskId
import io.codestream.core.api.events.*
import io.codestream.core.runtime.container.TaskScope
import io.codestream.core.runtime.container.TaskScopeId
import io.codestream.core.runtime.tree.DefaultLeaf
import io.codestream.util.Timer


class SimpleTaskHandler(val taskId: TaskId, val taskDef: TaskDef)
    : DefaultLeaf<StreamContext>(taskId.stringId, {
    val task = it.get<SimpleTask>(taskId)
            ?: throw ComponentFailedException(taskId.stringId, "unable to resolve component")

    try {
        TaskDefContext.defn = taskDef
        if (taskDef.condition(it.bindings)) {
            it.events.publish(BeforeTaskEvent(taskId, "running", TaskState.running))
            val runTime = Timer.run {
                task.run(it.bindings)
            }
            it.events.publish(AfterTaskEvent(taskId, "completed", TaskState.completed, runTime.first))
        } else {
            it.events.publish(TaskSkippedEvent(taskId, "Task condition evaluated to false"))
        }
    } catch (e:Exception) {
        it.events.publish(TaskErrorEvent(taskId, TaskError(e)))
        throw e
    } finally {
        TaskDefContext.clear()
        TaskScope.release(TaskScopeId(it, taskId))
    }


}) {
    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}