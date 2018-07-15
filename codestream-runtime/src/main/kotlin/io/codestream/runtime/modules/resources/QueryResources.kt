package io.codestream.runtime.modules.resources

import io.codestream.api.ComponentFailedException
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.resources.ResourceType
import io.codestream.api.resources.WritableResourceRepository

@Task(name = "query", description = "Queries the resources repository against a defined set of parameters")
class QueryResources(
        @Parameter(description = "Name of resource type to define")
        val type: String,
        @Parameter(description = "Name of the output variable")
        override val outputVariable: String = "__output",
        @Parameter(description = "Parameters against which to query")
        val parameters: Map<String, Any?>
) : FunctionalTask {

    override fun getResult(ctx: RunContext): Any? {
        val registry = ctx["_resources"] as WritableResourceRepository?
                ?: throw ComponentFailedException("get-resource", "no resource registry defined")
        val resourceType = ResourceType.fromString(type)
        return registry.find(resourceType, parameters)
    }
}