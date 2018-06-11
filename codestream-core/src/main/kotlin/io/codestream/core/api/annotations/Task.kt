package io.codestream.core.api.annotations

annotation class Task(
        val name: String,
        val description: String,
        val required:Boolean = true,
        val allowedValues:Array<String> = arrayOf(),
        val regex:String = ""
)