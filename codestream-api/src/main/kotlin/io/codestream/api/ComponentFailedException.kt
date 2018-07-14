package io.codestream.api

class ComponentFailedException(
        componentId: String,
        msg: String
) : CodeStreamRuntimeException(componentId, msg)