/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.api

import io.codestream.api.Type

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

class ValidationErrors(val validationErrors:MutableSet<ValidationError> = linkedSetOf()) : Exception("Validation Error") {


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
