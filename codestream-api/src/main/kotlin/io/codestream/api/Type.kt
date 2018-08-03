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

import io.codestream.api.resources.Resource
import io.codestream.util.crypto.Secret
import io.codestream.util.transformation.TransformerService
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

enum class Type(
        val stringDescriptor: String,
        val typeMapping: KClass<*>,
        val description: String,
        val isArray: Boolean = false,
        val supportsRegexCheck: Boolean = false

) {


    any("any", Any::class, "Any type", false, true),
    iterable("iterable", Iterable::class, "Any type which can be iterated  over", true, false),
    string("string", String::class, "Standard string", false, true),
    int("int", Int::class, "32 bit signed integer value"),
    long("long", Long::class, "64 bit signed integer value"),
    boolean("boolean", Boolean::class, "true,false, yes, no"),
    float("float", Float::class, "32 bit signed floating point value"),
    double("double", Double::class, "64 bit signed integer value"),
    path("path", File::class, "Reference to a file on the file system"),
    enumeration("enum", Enum::class, "Fixed enumerated value"),
    anyArray("any[]", Array<Any>::class, "Array of any types", true),
    fileArray("path[]", Array<File>::class, "Array of file types", true),
    stringArray("string[]", Array<String>::class, "Array of string types", true, true),
    intArray("int[]", Array<Int>::class, "Array of int types", true),
    longArray("long[]", Array<Long>::class, "Array of long types", true),
    floatArray("float[]", Array<Float>::class, "Array of float types", true),
    doubleArray("double[]", Array<Double>::class, "Array of double types", true),
    iterator("iterator", Iterator::class,"Enumeration type", true),
    keyValue("keyValue", Map::class, "Set of key=value mappings"),
    resource("resource", Resource::class, "Resource stored in the resource registry"),
    secret("secret", Secret::class, "A value which needs to be securely stored"),
    date("date", LocalDate::class, "A date value"),
    time("time", LocalTime::class, "A time value"),
    datetime("datetime", LocalDateTime::class, "A timestamp value");


    fun <T> fromString(value: String?): T {
        @Suppress("UNCHECKED_CAST")
        return TransformerService.convertWithNull<String>(value, typeMapping) as T
    }

    fun <T> convert(value: Any?): T? {
        if (value == null) {
            return null
        }
        @Suppress("UNCHECKED_CAST")
        return TransformerService.convertWithNull<Any>(value, typeMapping) as T
    }

    companion object {

        private val synonyms = mapOf<KClass<*>, KClass<*>>(
                IntArray::class to Array<Int>::class,
                LongArray::class to Array<Long>::class,
                FloatArray::class to Array<Float>::class,
                ShortArray::class to Array<Short>::class,
                ByteArray::class to Array<Byte>::class,
                BooleanArray::class to Array<Boolean>::class,
                DoubleArray::class to Array<Double>::class
        )

        fun stringType(type: KClass<*>): String? = typeForClass(type)?.stringDescriptor
        fun typeForString(type: String): Type? = values().singleOrNull { it.stringDescriptor.equals(type) }
        fun typeForClass(type: KClass<*>): Type? {

            if (type.isSubclassOf(Enum::class)) {
                return enumeration
            }

            val resolvedType = synonyms[type] ?: type

            return values().singleOrNull { it.typeMapping.equals(resolvedType) }
        }
    }


}