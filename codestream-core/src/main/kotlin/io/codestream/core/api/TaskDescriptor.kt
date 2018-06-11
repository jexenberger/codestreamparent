package io.codestream.core.api

import io.codestream.di.api.Factory

data class TaskDescriptor(
        val module: Module,
        val name: String,
        val description: String,
        val properties: Map<String, ParameterDescriptor>,
        val factory: Factory<Task>
)