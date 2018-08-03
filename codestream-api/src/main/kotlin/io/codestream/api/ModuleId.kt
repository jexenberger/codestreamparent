/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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