package io.codestream.core.runtime

import io.codestream.core.api.Module.Companion.defaultVersion
import io.codestream.core.api.ModuleDoesNotExistException
import io.codestream.core.api.TaskDescriptor
import io.codestream.core.api.TaskType

object TaskRegistry {

    fun resolve(type: TaskType, callingModule:CodeStreamModule? = null) : TaskDescriptor? {
        val version =  callingModule?.getDependencyVersion(type.module)
        val module = version?.let { ModuleRegistry[ModuleId(type.module, version)] }
                ?: ModuleRegistry.getLatestVersion(type.module)
        val resolvedModule = module ?: throw ModuleDoesNotExistException("${type.module}@${CodeStreamModule.versionString(version ?: defaultVersion)}")
        return resolvedModule[type]
    }

}