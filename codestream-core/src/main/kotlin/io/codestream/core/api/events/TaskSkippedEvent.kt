package io.codestream.core.api.events

import io.codestream.core.api.TaskId

class TaskSkippedEvent(taskId: TaskId, desc: String) : TaskEvent(taskId, desc, TaskState.notRun)