package io.codestream.api

import io.codestream.runtime.Type

enum class ErrorType() {
    required,
    invalidValue,
    invalidFormat,
    invalidType,
    notSupported
}

data class ValidationError(val type: ErrorType, val reference:String, val description:String) {
    override fun toString(): String {
        return "[$type]::['$reference']: '$description')"
    }
}

class ValidationErrors(val validationErrors:MutableSet<ValidationError> = linkedSetOf()) {


    fun required(name: String): ValidationErrors {
        validationErrors += ValidationError(ErrorType.required, name, "is required")
        return this
    }

    fun invalidValueForList(name: String, allowedValues: Array<String>): ValidationErrors {
        validationErrors += ValidationError(ErrorType.invalidValue, name, "Value not allowed must be one of : $allowedValues")
        return this
    }

    fun invalidFormat(name: String, regex: String): ValidationErrors {
        validationErrors += ValidationError(ErrorType.invalidFormat, name, "Is not in the required format : $regex")
        return this
    }

    fun invalidType(name: String, type: Type): ValidationErrors {
        validationErrors += ValidationError(ErrorType.invalidType, name, "Is not convertable to target type: '${type.name}'")
        return this
    }

    fun notSupported(name: String, msg:String): ValidationErrors {
        validationErrors += ValidationError(ErrorType.notSupported, name, msg)
        return this
    }

    fun merge(other: ValidationErrors) : ValidationErrors {
        this.validationErrors.addAll(other.validationErrors)
        return this
    }

    fun valid() : ValidationErrors? = if (this.validationErrors.isNotEmpty()) this else null

    override fun toString(): String {
        return validationErrors.map { it.toString() }.joinToString("; ")
    }

    fun toStringWithDescriptions(): String {
        return validationErrors.map { it.description }.joinToString("; ")
    }
}
