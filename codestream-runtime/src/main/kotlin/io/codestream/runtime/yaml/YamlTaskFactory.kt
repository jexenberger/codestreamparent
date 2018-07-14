package io.codestream.runtime.yaml

import io.codestream.api.Task
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import io.codestream.api.TaskId
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory

class YamlTaskFactory(val module: BaseYamlModule) : Factory<Task> {
    override fun get(id: ComponentId, ctx: Context): CompositeTask {
        val streamContext = ctx as StreamContext
        val compositeTask = module.getCompositeTask(id as TaskId, streamContext)
        streamContext.registerTask(id, compositeTask)
        compositeTask.registerTask(module)
        return compositeTask
    }

}