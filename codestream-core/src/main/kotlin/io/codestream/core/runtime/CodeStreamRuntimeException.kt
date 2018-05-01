package io.codestream.core.runtime

import io.codestream.util.SystemException


abstract class CodeStreamRuntimeException(
        val componentId: ComponentId,
        val msg: String
) : SystemException(createMsg(componentId, msg)) {


    companion object {
         fun createMsg(componentId: ComponentId, msg: String) =
                "[$componentId] -> $msg"
    }
}

