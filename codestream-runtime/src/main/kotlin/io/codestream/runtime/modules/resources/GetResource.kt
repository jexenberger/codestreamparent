package io.codestream.runtime.modules.resources

import io.codestream.api.ComponentFailedException
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.resources.*


@Task(name = "get", description = "Defines a resource against a specific type with an id, if the type and id combination exist the existing resource is overwritten")
class GetResource(
        @Parameter(description = "id to the resource to define")
        val id: String,
        @Parameter(description = "Name of resource type to define")
        val type: String,
        @Parameter(description = "Name of the output variable")
        override val outputVariable: String = "__output"
) : FunctionalTask {

    override fun getResult(ctx: RunContext): Any? {
        val registry = ctx["_resources"] as WritableResourceRepository? ?: throw ComponentFailedException("get-resource","no resource registry defined")
        val resourceType = ResourceType.fromString(type)
        return registry.get(resourceType, id)
    }
}