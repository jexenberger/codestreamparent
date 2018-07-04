package io.codestream.core.api.events

import io.codestream.core.api.TaskId

class AfterTaskEvent(taskId: TaskId, desc: String, state: TaskState, val timeTaken: Long) : TaskEvent(taskId, desc, state)