package io.codestream.api.metamodel

import io.codestream.api.RunContext
import io.codestream.api.TaskId

open class TaskDef(
        val id: TaskId,
        val parameters: Map<String, ParameterDef>,
        val condition: (RunContext) -> Boolean = { true }
)