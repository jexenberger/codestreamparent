package io.codestream.core.api

class ComponentPropertyFailureException(
        componentId: String,
        val property: String,
        msg: String
) : CodeStreamRuntimeException("${componentId}.${property}", msg)