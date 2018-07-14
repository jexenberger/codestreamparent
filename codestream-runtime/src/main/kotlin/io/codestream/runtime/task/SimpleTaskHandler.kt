package io.codestream.runtime.task

import io.codestream.api.ComponentFailedException
import io.codestream.api.SimpleTask
import io.codestream.api.TaskError
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.StreamContext
import io.codestream.api.TaskId
import io.codestream.api.events.*
import io.codestream.runtime.container.TaskScope
import io.codestream.runtime.container.TaskScopeId
import io.codestream.runtime.tree.DefaultLeaf
import io.codestream.util.Timer


class SimpleTaskHandler(val taskId: TaskId, val taskDef: TaskDef)
    : DefaultLeaf<StreamContext>(taskId.stringId, {
    val task = it.get<SimpleTask>(taskId)
            ?: throw ComponentFailedException(taskId.stringId, "unable to resolve component")

    try {
        io.codestream.runtime.task.TaskDefContext.defn = taskDef
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
        it.events.publish(TaskErrorEvent(taskId, TaskError(e, it.bindings)))
        throw e
    } finally {
        io.codestream.runtime.task.TaskDefContext.clear()
        TaskScope.release(TaskScopeId(it, taskId))
    }


}) {
    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}