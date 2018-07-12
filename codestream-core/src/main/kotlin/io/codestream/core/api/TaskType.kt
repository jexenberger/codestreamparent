package io.codestream.core.api

import io.codestream.core.runtime.ModuleRegistry

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
                val type = TaskType(ModuleRegistry.systemModuleId, str)
                return type
            }

            val parts = str.split("::")
            if (parts.size != 2) {
                throw IllegalArgumentException("$str is not a valid format, must [module]::[task]")
            }
            val module = parts[0]
            return TaskType(ModuleId.fromString(module), parts[1])
        }
    }

    override fun toString(): String {
        return taskName
    }
}