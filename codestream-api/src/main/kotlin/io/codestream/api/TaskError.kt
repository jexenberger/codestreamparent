package io.codestream.api

import io.codestream.util.rootCause
import io.codestream.util.stackDump

class TaskError(val taskId: TaskId, val exception: Exception, val runContext: RunContext) : Exception("$taskId -> '$exception'") {
    val rootCause: String = exception!!.rootCause.stackDump
}