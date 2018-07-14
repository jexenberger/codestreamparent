package io.codestream.api.events

import io.codestream.api.TaskId

class BeforeTaskEvent(taskId: TaskId, desc: String, state:TaskState) : TaskEvent(taskId, desc, state)