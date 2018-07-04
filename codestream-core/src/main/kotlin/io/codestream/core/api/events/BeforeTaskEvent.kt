package io.codestream.core.api.events

import io.codestream.core.api.TaskId

class BeforeTaskEvent(taskId: TaskId, desc: String, state:TaskState) : TaskEvent(taskId, desc, state)