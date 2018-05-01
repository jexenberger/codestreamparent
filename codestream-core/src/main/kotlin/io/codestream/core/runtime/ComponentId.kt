package io.codestream.core.runtime

data class ComponentId(
        val stream:String,
        val id:String,
        val type:ComponentType
)