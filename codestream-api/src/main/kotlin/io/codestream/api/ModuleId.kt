package io.codestream.api

import de.skuzzle.semantic.Version


data class ModuleId(val name: String, val version: Version = defaultVersion) {

    val isDefaultVersion get() = version.equals(defaultVersion)


    override fun toString(): String {
        return "$name@${CodestreamModule.versionString(version)}"
    }


    companion object {
        fun fromString(module: String): ModuleId {
            val modParts = module.split("@")
            if (modParts.size == 2) {
                try {
                    val ver = if (modParts[1].equals("LATEST", true)) defaultVersion else Version.parseVersion(modParts[1])
                    return ModuleId(modParts[0], ver)
                } catch (e: Version.VersionFormatException) {
                    throw IllegalArgumentException("$module must be in format <module(@version)")
                }
            } else {
                return ModuleId(module, defaultVersion)
            }
        }
    }


}