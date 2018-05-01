package io.codestream.core.metamodel

import io.codestream.core.runtime.CoreType

data class PropertyDef(
        val name:String,
        val valueDefn:String,
        val type:CoreType
)