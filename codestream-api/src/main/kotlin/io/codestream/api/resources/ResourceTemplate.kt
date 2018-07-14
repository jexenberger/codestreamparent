package io.codestream.api.resources

import io.codestream.api.descriptor.ParameterDescriptor

data class ResourceTemplate(
        val type: ResourceType,
        val description:String,
        val parameters:Map<String, ParameterDescriptor>)