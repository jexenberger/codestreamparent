package io.codestream.core.api.resources

object ResourceTypeRegistry {

    private val registry:MutableMap<ResourceType, ResourceTemplate> = mutableMapOf()


    operator fun plusAssign(template:ResourceTemplate) {
        registry[template.type] = template
    }

    operator fun get(type:ResourceType) : ResourceTemplate? {
        return registry[type]
    }

}