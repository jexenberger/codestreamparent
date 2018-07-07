package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.runtime.ModuleRegistry


data class ModuleId(val name: String, val version: Version = CodestreamModule.defaultVersion) {

    val defaultVersion get() =  version.equals(CodestreamModule.defaultVersion)

    val module:CodestreamModule get() = ModuleRegistry[this] ?: throw IllegalStateException("${toString()} does not exist for this or not for the specified version")

    override fun toString(): String {
        return "$name@${CodestreamModule.versionString(version)}"
    }


}