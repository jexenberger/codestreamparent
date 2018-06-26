package io.codestream.core.api.resources

import io.codestream.core.api.ParameterDescriptor

data class ResourceTemplate(
        val name:String,
        val description:String,
        val parameters:Map<String, ParameterDescriptor>)