package io.codestream.api.resources

import io.codestream.api.ResourceException
import java.io.File

data class ResourceType(val category: String, val name: String) {


    val pathSnippet: File = File(category, name)
    val qualifiedName = "$category::$name"

    override fun toString(): String {
        return qualifiedName
    }

    companion object {
        fun fromString(str: String): ResourceType {
            val parts = str.split("::")
            return ResourceType(parts[0], parts[1])
        }
    }
}