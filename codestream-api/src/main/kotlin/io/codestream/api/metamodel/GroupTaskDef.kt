package io.codestream.api.metamodel

import io.codestream.api.RunContext
import io.codestream.api.TaskId

class GroupTaskDef(
        id: TaskId,
        parameters: Map<String, ParameterDef>,
        val paralell: Boolean,
        condition: (RunContext) -> Boolean = { true })
    : TaskDef(
        id,
        parameters,
        condition
)