package io.codestream.core.runtime.task

import io.codestream.core.metamodel.TaskDef
import io.codestream.core.api.Task
import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.tree.DefaultLeaf
import io.codestream.core.runtime.tree.Leaf
import io.codestream.core.runtime.tree.NodeState
import io.codestream.di.api.ApplicationContext
import io.codestream.di.api.Context

class SimpleTaskHandler(val taskId:TaskId, val taskDef:TaskDef, val application:ApplicationContext)
    : DefaultLeaf<StreamContext>(taskId.stringId,  {
    val task = application.get<Task>(taskId)!!

    if (taskDef.condition(it.bindings)) {
        task.run(taskDef, it.bindings)
    }

})