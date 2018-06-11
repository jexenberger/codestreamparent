package io.codestream.di.api

import io.codestream.di.annotation.WiredConstructor
import io.codestream.util.noArgsConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

fun resolveConstructor(type: KClass<*>): KFunction<*>? {
    val constructor = type.constructors.find { it.findAnnotation<WiredConstructor>() != null }
    if (constructor != null) {
        return constructor
    }
    val noConstructors = type.constructors.size
    val candidate = if (noConstructors > 1) type.noArgsConstructor else type.constructors.iterator().next()
    return candidate
}