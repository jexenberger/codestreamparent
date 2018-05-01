package io.codestream.core.metamodel

class GroupComponentDef(
        stream: StreamDef,
        val paralell: Boolean,
        id: String,
        condition: ConditionDef,
        type: Pair<String, String>,
        name: String,
        properties: Set<PropertyDef>,
        val onError: ComponentDef,
        val finallyBlock: ComponentDef)
    : ComponentDef(
        stream,
        id,
        condition,
        type,
        name,
        properties
)