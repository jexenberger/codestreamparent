package io.codestream.api.annotations

annotation class Task(
        val name: String,
        val description: String,
        val returnDescription: String = "")