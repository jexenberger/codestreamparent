package io.codestream.core.metamodel

import io.codestream.core.api.ParameterDescriptor

data class PropertyDef(
        val name: String,
        val valueDefn: Any?,
        val type: ParameterDescriptor
)