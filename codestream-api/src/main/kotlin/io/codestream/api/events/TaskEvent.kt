package io.codestream.api.events

import io.codestream.api.TaskId

open class TaskEvent(val taskId: TaskId, desc: String, val state: TaskState) : CodestreamEvent(desc)