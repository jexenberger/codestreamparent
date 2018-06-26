package io.codestream.core.api

import io.codestream.util.rootCause
import io.codestream.util.stackDump

data class TaskError(val exception:Exception) {
    val message:String = exception.message!!
    val rootCause:String = exception!!.rootCause.stackDump
}