package io.codestream.core.api

import io.codestream.core.runtime.Type

enum class ErrorType() {
    required,
    invalidValue,
    invalidFormat,
    invalidType,
    notSupported
}

data class Error(val type:ErrorType,val reference:String, val description:String)

class ValidationError(val errors:MutableSet<Error> = linkedSetOf()) {


    fun required(name: String): ValidationError {
        errors += Error(ErrorType.required, name, "is required")
        return this
    }

    fun invalidValueForList(name: String, allowedValues: Array<String>): ValidationError {
        errors += Error(ErrorType.invalidValue, name, "Value not allowed must be one of : $allowedValues")
        return this
    }

    fun invalidFormat(name: String, regex: String): ValidationError {
        errors += Error(ErrorType.invalidFormat, name, "Is not in the required format : $regex")
        return this
    }

    fun invalidType(name: String, type: Type): ValidationError {
        errors += Error(ErrorType.invalidType, name, "Is not convertable to target type: '${type.name}'")
        return this
    }

    fun notSupported(name: String, msg:String): ValidationError {
        errors += Error(ErrorType.notSupported, name, msg)
        return this
    }

    fun merge(other:ValidationError) : ValidationError {
        this.errors.addAll(other.errors)
        return this
    }

    fun valid() : ValidationError? = if (this.errors.isNotEmpty()) this else null

}
