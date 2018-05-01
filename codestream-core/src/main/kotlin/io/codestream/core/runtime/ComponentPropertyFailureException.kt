package io.codestream.core.runtime

class ComponentPropertyFailureException(
        componentId: ComponentId,
        val property:String,
        msg:String
) : CodeStreamRuntimeException(componentId, msg)