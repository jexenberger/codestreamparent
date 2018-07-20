package io.codestream.runtime.modules.resources

import io.codestream.api.ComponentFailedException
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.resources.Resource
import io.codestream.api.resources.ResourceType
import io.codestream.api.resources.WritableResourceRepository

@Task(name = "query", description = "Queries the resources repository against a defined set of parameters")
class QueryResources(
        @Parameter(description = "Name of resource type to define")
        val type: String,
        @Parameter(description = "Parameters against which to query")
        val parameters: Map<String, Any?>
) : FunctionalTask<Collection<Resource>> {

    override fun evaluate(ctx: RunContext): Collection<Resource>? {
        val registry = ctx["_resources"] as WritableResourceRepository?
                ?: throw ComponentFailedException("get-resource", "no resource registry defined")
        val resourceType = ResourceType.fromString(type)
        return registry.find(resourceType, parameters)
    }
}