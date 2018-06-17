package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.runtime.ModuleId

data class TaskType(
        val module: ModuleId,
        val name: String
) {
    val taskName: String get() = "${module}::${name}"


    companion object {
        fun fromString(str: String): TaskType {
            val parts = str.split("::")
            if (parts.size < 2 || parts.size > 3) {
                throw IllegalArgumentException("$str is not a valid format, must [module]::[version]::[task] or [module]::[task]")
            }
            val module = if (parts.size == 3)
                ModuleId(parts[0], Version.parseVersion(parts[1]))
            else
                ModuleId(parts[0])
            val name = if (parts.size == 3)
                parts[1]
            else
                parts[2]
            return TaskType(module, name)
        }
    }
}