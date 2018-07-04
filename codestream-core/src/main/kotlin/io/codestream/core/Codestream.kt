package io.codestream.core

import java.io.File

import io.codestream.core.api.CodestreamModule
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.TaskType
import io.codestream.core.api.descriptor.TaskDescriptor
import io.codestream.core.api.metamodel.TaskDef
import io.codestream.core.api.resources.ResourceRepository
import io.codestream.core.runtime.ModuleRegistry
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.yaml.YamlModule
import io.codestream.di.event.EventHandler

class Codestream(
        val modules: Set<CodestreamModule>,
        val resources:ResourceRepository
) {


    fun getTask(path:File) : TaskDescriptor {
        val parent = path.parentFile
        val yamlModule = YamlModule(parent)
        ModuleRegistry += yamlModule
        return yamlModule[TaskType(parent.name, path.name)]!!
    }

    fun getTask(type:TaskType) : TaskDescriptor? {
        return null;
    }

    fun runTask(def:TaskDef, handler: EventHandler<*>, callingModule: CodestreamModule?) {
        val ctx = StreamContext()
        ctx.events.register(handler)
        ctx.registerTask(def, callingModule)
        val task = ctx.get<SimpleTask>(def.id)!!
        task.run(ctx.bindings)
    }

}