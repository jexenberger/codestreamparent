package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.runtime.ModuleId

interface Module {

    val name: String
    val description: String
    val version: Version

    val id: ModuleId
        get() = ModuleId(name, version)

    val tasks: Map<TaskType, TaskDescriptor>

    val dependencies: Set<ModuleId> get() = emptySet()

    operator fun get(name: TaskType): TaskDescriptor?

    fun getByName(name:String) = tasks[TaskType(id.name, name)]

    fun getDependencyVersion(moduleName: String): Version {
        return dependencies.find { it.name.equals(moduleName) }?.version ?: defaultVersion
    }


    companion object {
        val defaultVersion = Version.create(999999999, 999999999, 999999999)

        fun versionString(version: Version) = if (version.equals(defaultVersion)) "LATEST" else version.toString()
    }
}