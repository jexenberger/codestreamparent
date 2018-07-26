package io.codestream.api

import io.codestream.util.rootCause
import io.codestream.util.stackDump

class TaskError(val taskId: TaskId, val exception: Exception, val runContext: RunContext) : Exception("$taskId -> '$exception'") {
    val rootCause: String = exception.rootCause.stackDump

    companion object {
        fun bubble(taskId: TaskId, exception: Exception, runContext: RunContext) : TaskError {
            return if (exception is TaskError) exception else TaskError(taskId, exception, runContext)
        }
    }
}