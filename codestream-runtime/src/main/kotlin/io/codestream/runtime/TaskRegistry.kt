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

package io.codestream.runtime

import io.codestream.api.*
import io.codestream.api.descriptor.TaskDescriptor

object TaskRegistry {

    fun resolve(type: TaskType, callingModule: CodestreamModule? = null) : TaskDescriptor? {
        val version = callingModule?.getDependencyVersion(type.moduleName)
        val targetVersion = version?.let { it } ?: type.module.version
        val module = io.codestream.runtime.ModuleRegistry[ModuleId(type.moduleName, targetVersion)]
        val resolvedModule = module ?: throw ModuleDoesNotExistException("${type.module}@${CodestreamModule.versionString(version ?: defaultVersion)}")
        return resolvedModule.getByName(type.name)
    }

}