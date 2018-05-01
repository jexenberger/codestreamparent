package io.codestream.core.runtime.kt

import io.codestream.core.runtime.CoreType

class KtPropertyType(
        val name:String,
        val type: CoreType,
        val alias: String,
        val description:String,
        val disableEvaluation:Boolean = false) {
}