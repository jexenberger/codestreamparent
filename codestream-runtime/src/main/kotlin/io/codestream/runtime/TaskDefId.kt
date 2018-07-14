package io.codestream.runtime

import io.codestream.api.TaskId
import io.codestream.di.api.ComponentId

data class TaskDefId(val taskId: TaskId) : ComponentId {
    override val stringId: String
        get() = "${taskId.stringId}::##DEFINITION##"
}