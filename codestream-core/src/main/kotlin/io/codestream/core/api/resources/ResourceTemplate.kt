package io.codestream.core.api.resources

import io.codestream.core.api.descriptor.ParameterDescriptor

data class ResourceTemplate(
        val type:ResourceType,
        val description:String,
        val parameters:Map<String, ParameterDescriptor>)