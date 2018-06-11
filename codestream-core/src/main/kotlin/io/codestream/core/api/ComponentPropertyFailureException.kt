package io.codestream.core.api

import io.codestream.core.runtime.TaskId

class ComponentPropertyFailureException(
        componentId: String,
        val property: String,
        msg: String
) : CodeStreamRuntimeException(componentId, msg)