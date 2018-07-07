package io.codestream.core.doc

data class ParameterDoc(
        val name: String,
        val description:String,
        val type:String,
        val required:Boolean,
        val pattern:String? = null) {
}