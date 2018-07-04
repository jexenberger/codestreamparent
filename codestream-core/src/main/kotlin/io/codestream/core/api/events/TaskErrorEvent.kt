package io.codestream.core.api.events

import io.codestream.core.api.TaskError
import io.codestream.core.api.TaskId

class TaskErrorEvent(taskId: TaskId, val error:TaskError) : TaskEvent(taskId, error.message, TaskState.failed)