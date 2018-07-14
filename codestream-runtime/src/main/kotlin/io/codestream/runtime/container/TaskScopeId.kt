package io.codestream.runtime.container

import io.codestream.runtime.StreamContext
import io.codestream.api.TaskId
import io.codestream.di.api.ComponentId

data class TaskScopeId(val ctx:StreamContext, val taskId: TaskId) : ComponentId {
    override val stringId = "$ctx->$taskId"

    override fun toString() = stringId


}