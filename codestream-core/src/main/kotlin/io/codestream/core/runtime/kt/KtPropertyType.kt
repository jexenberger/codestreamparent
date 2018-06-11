package io.codestream.core.runtime.kt

import io.codestream.core.runtime.Type

class KtPropertyType(
        val name: String,
        val type: Type,
        val alias: String,
        val description: String,
        val disableEvaluation: Boolean = false) {
}