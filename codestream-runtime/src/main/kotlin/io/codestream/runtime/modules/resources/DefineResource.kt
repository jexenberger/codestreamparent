package io.codestream.runtime.modules.resources

import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.resources.*


@Task(name = "define", description = "Defines a resource against a specific type")
class DefineResource(
        @Parameter(description = "id to the resource to define")
        val id:String,
        @Parameter(description = "Name of resource type to define")
        val type:String,
        @Parameter(description = "Parameters to define the resource")
        val parameters:Map<String, Any?>
) : SimpleTask {


    override fun run(ctx: RunContext) {
        val registry = ctx["_resources"] as WritableResourceRepository?
        val resourceType = ResourceType.fromString(type)
        val resource = Resource(resourceType, id, parameters)
        registry?.save(resource)
    }
}