package io.codestream.core.runtime

import de.skuzzle.semantic.Version
import io.codestream.core.api.Module

data class ModuleId(val name: String, val version: Version = Module.defaultVersion) {

    val defaultVersion get() =  version.equals(Module.defaultVersion)

    val module:Module get() = ModuleRegistry[this] ?: throw IllegalStateException("${toString()} does not exist for this or not for the specified version")

    override fun toString(): String {
        return "$name::${Module.versionString(version)}"
    }
}