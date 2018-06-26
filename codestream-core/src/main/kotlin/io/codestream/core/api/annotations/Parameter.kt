package io.codestream.core.api.annotations

annotation class Parameter(
        val alias: String = "",
        val description: String,
        val required:Boolean = true,
        val allowedValues:Array<String> = emptyArray(),
        val regex:String = "",
        val default:String = "",
        val overrideType:String = ""
)