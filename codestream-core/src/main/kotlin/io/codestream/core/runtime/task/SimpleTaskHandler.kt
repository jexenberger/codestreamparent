package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.SimpleTask
import io.codestream.core.runtime.metamodel.TaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.container.TaskScope
import io.codestream.core.runtime.container.TaskScopeId
import io.codestream.core.runtime.tree.DefaultLeaf


class SimpleTaskHandler(val taskId: TaskId, val taskDef: TaskDef)
    : DefaultLeaf<StreamContext>(taskId.stringId, {
    val task = it.get<SimpleTask>(taskId)
            ?: throw ComponentFailedException(taskId.stringId, "unable to resolve component")

    try {
        TaskDefContext.defn = taskDef
        if (taskDef.condition(it.bindings)) {
            task.run(it.bindings)
        }
    } finally {
        TaskDefContext.clear()
        TaskScope.release(TaskScopeId(it, taskId))
    }


}) {
    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}