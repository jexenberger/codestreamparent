package io.codestream.core.metamodel

data class StreamDef(
        val name: String,
        val module: String,
        val tasks: Collection<TaskDef>
)