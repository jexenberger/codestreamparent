package io.codestream.core.runtime

class ComponentFailedException(
        componentId: ComponentId,
        msg:String
) : CodeStreamRuntimeException(componentId, msg)