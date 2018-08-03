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


data class TaskType(
        val module: ModuleId,
        val name: String
) {
    val taskName: String get() = "${module}::${name}"
    val moduleName: String get() = module.name

    companion object {
        fun fromString(str: String): TaskType {
            val delimiterIdx = str.indexOf("::")
            if (delimiterIdx == -1) {
                val type = TaskType(systemModuleId, str)
                return type
            }

            val parts = str.split("::")
            if (parts.size != 2) {
                throw IllegalArgumentException("$str is not a valid format, must [module(@version)]::[task]")
            }
            val module = parts[0]
            return TaskType(ModuleId.fromString(module), parts[1])
        }
    }

    override fun toString(): String {
        return taskName
    }
}