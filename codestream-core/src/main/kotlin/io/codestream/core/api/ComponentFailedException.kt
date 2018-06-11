package io.codestream.core.api

class ComponentFailedException(
        componentId: String,
        msg: String
) : CodeStreamRuntimeException(componentId, msg)