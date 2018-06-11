package io.codestream.core.api

import de.skuzzle.semantic.Version

interface Module {


    val name: String
    val description: String
    val version: Version

    val id: String
        get() = "$name::$version"

    val tasks: Map<String, TaskDescriptor>

    operator fun get(name: String) : TaskDescriptor?


}