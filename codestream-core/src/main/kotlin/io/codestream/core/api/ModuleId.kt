package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.runtime.ModuleRegistry
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.warn


data class ModuleId(val name: String, val version: Version = CodestreamModule.defaultVersion) {

    val defaultVersion get() =  version.equals(CodestreamModule.defaultVersion)

    val module:CodestreamModule get() = ModuleRegistry[this] ?: throw IllegalStateException("${toString()} does not exist for this or not for the specified version")

    override fun toString(): String {
        return "$name@${CodestreamModule.versionString(version)}"
    }


    companion object {
        fun fromString(module:String) : ModuleId {
            val modParts = module.split("@")
            if (modParts.size == 2) {
                try {
                    return ModuleId(modParts[0], Version.parseVersion(modParts[1]))
                } catch (e: Version.VersionFormatException) {
                    throw IllegalArgumentException("$module must be in format <module(@version)")
                }
            } else {
                return ModuleId(module, CodestreamModule.defaultVersion)
            }
        }
    }


}