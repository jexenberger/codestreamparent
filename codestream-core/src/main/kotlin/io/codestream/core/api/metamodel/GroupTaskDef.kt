package io.codestream.core.api.metamodel

import io.codestream.core.api.RunContext
import io.codestream.core.api.TaskId
import io.codestream.core.runtime.task.defaultCondition

class GroupTaskDef(
        id: TaskId,
        parameters: Map<String, ParameterDef>,
        val paralell: Boolean,
        condition: (RunContext) -> Boolean = defaultCondition)
    : TaskDef(
        id,
        parameters,
        condition
)