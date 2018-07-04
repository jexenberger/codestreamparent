package io.codestream.core.api.descriptor

import io.codestream.core.api.ComponentDefinitionException
import io.codestream.core.api.ValidationErrors
import io.codestream.core.runtime.Type
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

data class ParameterDescriptor(
        val name: String,
        val description: String,
        val type: Type,
        val required: Boolean = true,
        val alias: String = "",
        val allowedValues: Array<String> = emptyArray(),
        val regex: String = "") {

    val id: String get() = if (this.alias.isNotEmpty()) alias.trim() else name
    private val regexPattern: Pattern? by lazy { if (regex.isNotEmpty()) Pattern.compile(regex) else null }

    fun isValid(candidate: Any?): ValidationErrors? {
        if (required && candidate == null) {
            return ValidationErrors().required(name)
        }
        if (candidate == null) {
            return null
        }
        if (allowedValues.isNotEmpty()) {
            val allPresent = when (candidate) {
                is Map<*,*> -> candidate.keys.fold(true) {curr, value-> curr and allowedValues.contains(value?.toString() ?: "")}
                is List<*> -> candidate.fold(true) {curr, value-> curr and allowedValues.contains(value?.toString() ?: "")}
                is Array<*> -> candidate.fold(true) {curr, value-> curr and allowedValues.contains(value?.toString() ?: "")}
                else -> allowedValues.contains(candidate.toString())
            }
            if (!allPresent) {
                return ValidationErrors().invalidValueForList(name, allowedValues)
            }
        }
        try {
            val usePattern = allowedValues.isEmpty() && regex.isNotBlank() && type.supportsRegexCheck
            if (usePattern && !(regexPattern?.matcher(candidate.toString().trim())?.matches()?.let { it } ?: true)) {
                return ValidationErrors().invalidFormat(name, regex)
            }
        } catch (e: PatternSyntaxException) {
            throw ComponentDefinitionException(name, "Defined invalid regex : '$regex'")
        }
        try {
            type.convert<Any>(candidate)
        } catch (e: Exception) {
            return ValidationErrors().invalidType(name, type)
        }
        return null
    }


}
