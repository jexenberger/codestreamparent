package io.codestream.core.runtime.yaml

import io.codestream.core.api.Task
import io.codestream.core.runtime.CompositeTask
import io.codestream.core.runtime.StreamContext
import io.codestream.core.api.TaskId
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory

class YamlTaskFactory(val module: YamlModule) : Factory<Task> {
    override fun get(id: ComponentId, ctx: Context): CompositeTask {
        val compositeTask = module.getCompositeTask(id as TaskId)
        val streamContext = ctx as StreamContext
        streamContext.registerTask(id, compositeTask)
        compositeTask.registerTask(module)
        return compositeTask
    }

}