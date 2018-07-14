package io.codestream.api

import io.codestream.api.TaskType
import io.codestream.di.api.ComponentId
import java.util.*

data class TaskId(val taskType: TaskType, val id: String = UUID.randomUUID().toString()) : ComponentId {
    override val stringId: String
        get() = "${taskType.taskName}(${id})"


    override fun toString(): String {
        return stringId
    }


}