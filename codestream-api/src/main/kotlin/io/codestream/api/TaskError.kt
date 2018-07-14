package io.codestream.api

import io.codestream.util.rootCause
import io.codestream.util.stackDump

class TaskError(val exception: Exception, val runContext: RunContext) : Exception(exception) {
    val rootCause: String = exception!!.rootCause.stackDump
}