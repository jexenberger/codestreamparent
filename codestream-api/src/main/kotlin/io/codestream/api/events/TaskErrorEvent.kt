package io.codestream.api.events

import io.codestream.api.TaskError
import io.codestream.api.TaskId

class TaskErrorEvent(taskId: TaskId, val error:TaskError) : TaskEvent(taskId, error.message!!, TaskState.failed)