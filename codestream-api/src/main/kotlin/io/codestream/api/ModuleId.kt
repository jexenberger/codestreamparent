package io.codestream.api

import de.skuzzle.semantic.Version


data class ModuleId(val name: String, val version: Version = CodestreamModule.defaultVersion) {

    val defaultVersion get() =  version.equals(CodestreamModule.defaultVersion)


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