package io.codestream.api

import io.codestream.util.SystemException


abstract class CodeStreamRuntimeException(
        val componentId: String,
        val msg: String
) : SystemException(createMsg(componentId, msg)) {


    companion object {
        fun createMsg(componentId: String, msg: String) =
                "[$componentId] -> $msg"
    }
}

