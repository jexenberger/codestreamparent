package io.codestream.api.events

import io.codestream.api.TaskId

class TaskSkippedEvent(taskId: TaskId, desc: String) : TaskEvent(taskId, desc, TaskState.notRun)