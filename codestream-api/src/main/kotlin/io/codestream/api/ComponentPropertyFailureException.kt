package io.codestream.api

class ComponentPropertyFailureException(
        componentId: String,
        val property: String,
        msg: String
) : CodeStreamRuntimeException("${componentId}.${property}", msg)