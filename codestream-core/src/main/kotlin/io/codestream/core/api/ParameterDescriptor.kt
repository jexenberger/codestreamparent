package io.codestream.core.api

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

    fun isValid(candidate: Any?): ValidationError? {
        if (required && candidate == null) {
            return ValidationError().required(name)
        }
        if (candidate == null) {
            return null
        }
        if (allowedValues.isNotEmpty() && allowedValues.find { it.equals(candidate.toString()) } == null) {
            return ValidationError().invalidValueForList(name, allowedValues)
        }
        try {
            val usePattern = allowedValues.isEmpty() && regex.isNotBlank() && type.supportsRegexCheck
            if (usePattern && !(regexPattern?.matcher(candidate.toString().trim())?.matches()?.let { it } ?: true)) {
                return ValidationError().invalidFormat(name, regex)
            }
        } catch (e: PatternSyntaxException) {
            throw ComponentDefinitionException(name, "Defined invalid regex : '$regex'")
        }
        try {
            type.fromString<Any>(candidate.toString().trim())
        } catch (e: Exception) {
            return ValidationError().invalidType(name, type)
        }
        return null
    }

    fun isValidArray(candidate: Array<Any>?): ValidationError? {
        if (!type.isArray) {
            return ValidationError().notSupported(name,"'$type' is not an array type")
        }
        if (required && candidate == null) {
            return ValidationError().required(name)
        }
        if (candidate == null) {
            return null
        }
        val parent = ValidationError()
        candidate.forEach { item -> isValid(item)?.let { parent.merge(it) } }
        return parent.valid()
    }


}
