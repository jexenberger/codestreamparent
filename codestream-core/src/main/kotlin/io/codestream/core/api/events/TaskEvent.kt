package io.codestream.core.api.events

import io.codestream.core.api.TaskId

open class TaskEvent(val taskId: TaskId, desc: String, val state: TaskState) : CodestreamEvent(desc)