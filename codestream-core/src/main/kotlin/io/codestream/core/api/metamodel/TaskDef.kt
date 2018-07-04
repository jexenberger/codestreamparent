package io.codestream.core.api.metamodel

import io.codestream.core.api.RunContext
import io.codestream.core.api.TaskId

open class TaskDef(
        val id: TaskId,
        val parameters: Map<String, ParameterDef>,
        val condition: (RunContext) -> Boolean = { true }
)