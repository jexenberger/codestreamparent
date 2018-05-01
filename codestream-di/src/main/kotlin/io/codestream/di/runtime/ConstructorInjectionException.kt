package io.codestream.di.runtime

import kotlin.reflect.KClass

class ConstructorInjectionException(val type:KClass<*>, msg:String) : RuntimeException("${type.qualifiedName!!} -> $msg")