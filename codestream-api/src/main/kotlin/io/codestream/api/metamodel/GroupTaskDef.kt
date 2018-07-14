package io.codestream.api.metamodel

import io.codestream.api.RunContext
import io.codestream.api.TaskId
import io.codestream.runtime.task.defaultCondition

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