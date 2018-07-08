package io.codestream.core.api

import io.codestream.util.rootCause
import io.codestream.util.stackDump

class TaskError(val exception: Exception, val runContext: RunContext) {
    val message: String = exception.message!!
    val rootCause: String = exception!!.rootCause.stackDump
}