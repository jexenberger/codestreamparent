package io.codestream.core.metamodel

import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.Type

open class TaskDef(
        val stream: StreamDef,
        val id: String,
        val condition: (TaskContext) -> Boolean,
        val type: Type,
        val name: String,
        val properties: Map<String, PropertyDef>
)