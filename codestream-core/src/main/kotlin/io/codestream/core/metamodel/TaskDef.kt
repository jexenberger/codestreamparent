package io.codestream.core.metamodel

import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.TaskId

open class TaskDef(
        val id: TaskId,
        val parameters: Map<String, ParameterDef>,
        val condition: (TaskContext) -> Boolean = { true }
)