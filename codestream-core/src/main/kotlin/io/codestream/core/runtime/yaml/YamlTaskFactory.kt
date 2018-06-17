package io.codestream.core.runtime.yaml

import io.codestream.core.api.Task
import io.codestream.core.runtime.TaskId
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory

class YamlTaskFactory(val onError:TaskId?, val onFinally:TaskId?) : Factory<Task> {
    override fun get(id: ComponentId, ctx: Context): YamlTask {
        return YamlTask(id as TaskId, onError, onFinally)
    }
}