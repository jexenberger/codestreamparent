package io.codestream.runtime

import io.codestream.api.CodestreamModule
import io.codestream.api.CodestreamModule.Companion.defaultVersion
import io.codestream.api.ModuleDoesNotExistException
import io.codestream.api.ModuleId
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.api.TaskType

object TaskRegistry {

    fun resolve(type: TaskType, callingModule: CodestreamModule? = null) : TaskDescriptor? {
        val version = callingModule?.getDependencyVersion(type.moduleName)
        val targetVersion = version?.let { it } ?: type.module.version
        val module = io.codestream.runtime.ModuleRegistry[ModuleId(type.moduleName, targetVersion)]
        val resolvedModule = module ?: throw ModuleDoesNotExistException("${type.module}@${CodestreamModule.versionString(version ?: defaultVersion)}")
        return resolvedModule.getByName(type.name)
    }

}