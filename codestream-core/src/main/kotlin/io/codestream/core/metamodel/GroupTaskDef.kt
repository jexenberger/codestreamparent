package io.codestream.core.metamodel

import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.Type
import io.codestream.core.runtime.task.defaultCondition

class GroupTaskDef(
        id: TaskId,
        parameters: Map<String, ParameterDef>,
        val paralell: Boolean,
        condition: (TaskContext) -> Boolean = defaultCondition,
        val onError: TaskDef? = null,
        val onFinally: TaskDef? = null)
    : TaskDef(
        id,
        parameters,
        condition
)