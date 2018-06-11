package io.codestream.core.runtime

import io.codestream.util.transformation.TransformerService
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

enum class Type(
        val stringDescriptor: String,
        val typeMapping: KClass<*>,
        val isArray: Boolean = false,
        val supportsRegexCheck: Boolean = false
) {
    string("string", String::class, false, true),
    int("int", Int::class),
    long("long", Long::class),
    boolean("boolean", Boolean::class),
    float("float", Float::class),
    double("double", Double::class),
    file("file", File::class),
    enumeration("enum", Enum::class),
    fileArray("file[]", Array<File>::class, true),
    stringArray("string[]", Array<String>::class, true, true),
    intArray("int[]", Array<Int>::class, true),
    longArray("long[]", Array<Long>::class, true),
    floatArray("float[]", Array<Int>::class, true),
    doubleArray("double[]", Array<Int>::class, true),
    keyValue("keyValue", Map::class);

    fun <T> fromString(value: String?) : T {
        @Suppress("UNCHECKED_CAST")
        return TransformerService.convertWithNull<String>(value, typeMapping) as T
    }

    fun <T> convert(value: Any?) : T? {
        if (value == null) {
            return null
        }
        @Suppress("UNCHECKED_CAST")
        return TransformerService.convertWithNull<Any>(value, value::class) as T
    }

    companion object {
        fun stringType(type: KClass<*>): String? = typeForClass(type)?.stringDescriptor

        fun typeForClass(type: KClass<*>): Type? = values()
                .filter { it.typeMapping.equals(type) || it.typeMapping.isSuperclassOf(type) }
                .singleOrNull()
    }


}