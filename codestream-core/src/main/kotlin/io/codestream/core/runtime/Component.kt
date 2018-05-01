package io.codestream.core.runtime

import io.codestream.core.metamodel.ComponentDef

interface Component {
    val type:ComponentType
    val id:ComponentId
    val condition:(defn:ComponentDef, ctx:Context) -> Boolean
    val properties:Map<String, Property>
}