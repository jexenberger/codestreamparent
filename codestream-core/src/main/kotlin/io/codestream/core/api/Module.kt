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

    operator fun get(name: TaskType) : TaskDescriptor?

    companion object {
        val defaultVersion = Version.create(999999999,0, 0)

        fun versionString(version:Version) = if (version.equals(defaultVersion))  "LATEST" else version.toString()
    }
}