package io.codestream.core.metamodel

open class ComponentDef(
        val stream:StreamDef,
        val id:String,
        val condition: ConditionDef,
        val type:Pair<String, String>,
        val name:String,
        val properties:Set<PropertyDef>
)