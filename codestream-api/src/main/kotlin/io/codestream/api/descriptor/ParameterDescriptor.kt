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

package io.codestream.api.descriptor

import io.codestream.api.ComponentDefinitionException
import io.codestream.api.Type
import io.codestream.api.ValidationErrors
import io.codestream.util.Either
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

data class ParameterDescriptor(
        val name: String,
        val description: String,
        val type: Type,
        val required: Boolean = true,
        val alias: String = "",
        val allowedValues: Array<String> = emptyArray(),
        val regex: String = "",
        val default: String? = null,
        val returnDescription: String? = null) {

    val id: String get() = if (this.alias.isNotEmpty()) alias.trim() else name
    private val regexPattern: Pattern? by lazy { if (regex.isNotEmpty()) Pattern.compile(regex) else null }
    val defaultValue: Any? get() = if (default.isNullOrBlank()) null else default


    fun isValid(candidate: Any?): Either<Any?, ValidationErrors> {
        if (required && candidate == null || (candidate is String && candidate.isEmpty())) {
            return Either.right(ValidationErrors().required(name))
        }
        if (candidate == null) {
            return Either.left(candidate)
        }
        if (allowedValues.isNotEmpty()) {
            val allPresent = when (candidate) {
                is Map<*, *> -> candidate.keys.fold(true) { curr, value ->
                    curr and allowedValues.contains(value?.toString() ?: "")
                }
                is List<*> -> candidate.fold(true) { curr, value ->
                    curr and allowedValues.contains(value?.toString() ?: "")
                }
                is Array<*> -> candidate.fold(true) { curr, value ->
                    curr and allowedValues.contains(value?.toString() ?: "")
                }
                else -> allowedValues.contains(candidate.toString())
            }
            if (!allPresent) {
                return Either.right(ValidationErrors().invalidValueForList(name, allowedValues))
            }
        }
        try {
            val usePattern = allowedValues.isEmpty() && regex.isNotBlank() && type.supportsRegexCheck
            if (usePattern && !(regexPattern?.matcher(candidate.toString().trim())?.matches()?.let { it } ?: true)) {
                return Either.right(ValidationErrors().invalidFormat(name, regex))
            }
        } catch (e: PatternSyntaxException) {
            throw ComponentDefinitionException(name, "Defined invalid regex : '$regex'")
        }
        try {
            return Either.left(type.convert(candidate))
        } catch (e: Exception) {
            return Either.right(ValidationErrors().invalidType(name, type))
        }
    }


}
