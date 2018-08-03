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

package io.codestream.runtime.modules.system

import de.skuzzle.semantic.Version
import io.codestream.api.ModuleId
import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter
import io.codestream.api.defaultVersion
import io.codestream.runtime.ModuleRegistry

class SystemModuleFunctions {

    @ModuleFunction("Checks if a module exists with a version string")
    fun moduleExists(
            @ModuleFunctionParameter(value = "name", description = "Name of the module to check")
            name:String,
            @ModuleFunctionParameter(value = "version", description = "Version in string format to check")
            version:String
    ) : Boolean {
        val ver = if (version.equals("LATEST", ignoreCase = true)) defaultVersion else Version.parseVersion(version)
        return ModuleRegistry[ModuleId(name, ver)] != null
    }


}