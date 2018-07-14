package io.codestream.api.events

import io.codestream.api.TaskId

class AfterTaskEvent(taskId: TaskId, desc: String, state: TaskState, val timeTaken: Long) : TaskEvent(taskId, desc, state)