package io.codestream.core.annotations

annotation class ComponentProperty(
        val alias: String = "",
        val description: String,
        val disableEvaluation: Boolean = false
)