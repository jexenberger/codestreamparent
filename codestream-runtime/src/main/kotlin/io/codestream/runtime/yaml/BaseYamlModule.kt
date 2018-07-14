package io.codestream.runtime.yaml

import io.codestream.api.CodestreamModule
import io.codestream.api.TaskDoesNotExistException
import io.codestream.api.TaskId
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import java.io.File

interface BaseYamlModule : CodestreamModule {


    val modulePath: String




    fun createScriptObjects(): Map<String, Any>

    fun getCompositeTask(id: TaskId, ctx: StreamContext): CompositeTask {
        val withThis = createScriptObjects()
        val taskDescriptor = getByName(id.taskType.name)
        taskDescriptor ?: throw TaskDoesNotExistException(id.taskType)
        val name = id.taskType.name
        val file = File(resolveTaskPath(name))
        val newContext = StreamContext(originatingContextId = ctx.originatingContextId)
        newContext.bindings["__modulePath"] = modulePath
        withThis.forEach { (k, v) ->
            newContext.bindings[k] = v
        }
        val task = CompositeTask(id, taskDescriptor, newContext)
        YamlTaskBuilder(name, this, file.readText()).defineTaskTree(task)
        return task
    }

    fun resolveTaskPath(name: String) : String

}