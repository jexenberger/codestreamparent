package io.codestream.di.runtime

import kotlin.reflect.KClass

class PropertyInjectionException(
        val type: KClass<*>,
        val property: String,
        val msg:String
) : RuntimeException("${type}.${property} -> $msg")