package io.codestream.api.resources

class ResolvingResourceRegistry(private var primaryResourceRepository: WritableResourceRepository) : WritableResourceRepository {

    private var registries: MutableCollection<ResourceRepository> = mutableSetOf(primaryResourceRepository)

    override fun save(resource: Resource) {
        primaryResourceRepository.save(resource)
    }

    override fun get(type: ResourceType, id: String): Resource? {
        for (registry in registries) {
            val lookup = registry[type, id]
            if (lookup != null) {
                return lookup
            }
        }
        return null
    }

    override fun find(type: ResourceType, attributes: Map<String, Any?>): Collection<Resource> {
        val found = mutableListOf<Resource>()
        for (registry in registries) {
            found.addAll(registry.find(type, attributes))
        }
        return found
    }

    override fun find(type: ResourceType): Collection<Resource> {
        val found = mutableListOf<Resource>()
        for (registry in registries) {
            found.addAll(registry.find(type))
        }
        return found
    }

    @Synchronized
    fun setPrimary(primaryResourceRepository: WritableResourceRepository) {
        this.primaryResourceRepository = primaryResourceRepository
    }


    operator fun plusAssign(repository: ResourceRepository) {
        synchronized(registries) {
            this.registries.add(repository)
        }
    }




}