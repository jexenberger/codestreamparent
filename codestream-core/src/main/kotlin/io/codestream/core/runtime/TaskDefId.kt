package io.codestream.core.runtime

import io.codestream.di.api.ComponentId

data class TaskDefId(val taskId:TaskId) : ComponentId {
    override val stringId: String
        get() = "${taskId.stringId}::##DEFINITION##"
}