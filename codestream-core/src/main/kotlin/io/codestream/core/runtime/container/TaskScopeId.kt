package io.codestream.core.runtime.container

import io.codestream.core.runtime.StreamContext
import io.codestream.core.api.TaskId
import io.codestream.di.api.ComponentId

data class TaskScopeId(val ctx:StreamContext, val taskId: TaskId) : ComponentId {
    override val stringId = "$ctx->$taskId"

    override fun toString() = stringId


}