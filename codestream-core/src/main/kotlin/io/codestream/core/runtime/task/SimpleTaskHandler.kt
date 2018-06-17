package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.SimpleTask
import io.codestream.core.metamodel.TaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.tree.DefaultLeaf
import io.codestream.di.api.ApplicationContext


class SimpleTaskHandler(val taskId: TaskId, val taskDef: TaskDef)
    : DefaultLeaf<StreamContext>(taskId.stringId, {
    val task = it.get<SimpleTask>(taskId)
            ?: throw ComponentFailedException(taskId.stringId, "unable to resolve component")

    if (taskDef.condition(it.bindings)) {
        task.run(taskDef, it.bindings)
    }

})