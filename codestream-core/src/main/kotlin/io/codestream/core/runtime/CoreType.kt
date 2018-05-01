package io.codestream.core.runtime

import kotlin.reflect.KClass
import java.io.File

enum class CoreType(
        val stringDescriptor:String,
        val typeMapping:KClass<*>,
        val isArray:Boolean = false
) {
    string("string",String::class),
    int("int",Int::class),
    long("long",Long::class),
    boolean("boolean",Boolean::class),
    float("float",Float::class),
    double("double",Double::class),
    file("file",File::class),
    enumeration("enum",Enum::class),
    fileArray("[file]",Array<File>::class),
    stringArray("[string]",Array<String>::class),
    intArray("[int]",Array<Int>::class),
    longArray("[long]",Array<Long>::class),
    floatArray("[float]",Array<Int>::class),
    doubleArray("[double]",Array<Int>::class),
    keyValue("[keyValue]",Map::class);

    companion object {
        fun stringType(type: KClass<*>): String? = typeForClass(type)?.stringDescriptor

        fun typeForClass(type: KClass<*>): CoreType? = values()
                .filter { it.typeMapping.equals(type)}
                .singleOrNull()
    }


}