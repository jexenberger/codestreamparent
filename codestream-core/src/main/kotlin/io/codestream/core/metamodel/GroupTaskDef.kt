package io.codestream.core.metamodel

import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.Type

class GroupTaskDef(
        stream: StreamDef,
        val paralell: Boolean,
        id: String,
        condition: (TaskContext) -> Boolean,
        type: Type,
        name: String,
        properties: Map<String, PropertyDef>,
        val onError: TaskDef,
        val finallyBlock: TaskDef)
    : TaskDef(
        stream,
        id,
        condition,
        type,
        name,
        properties
)