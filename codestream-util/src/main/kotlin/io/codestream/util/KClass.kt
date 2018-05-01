package io.codestream.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

val <T : Any> KClass<T>.mutableProperties: Collection<KMutableProperty<*>>
    get() = this.memberProperties.filter { it is KMutableProperty<*> }.map { it as KMutableProperty<*> }

fun <T : Any> KClass<T>.mutablePropertyByName(name: String) =
        this.memberProperties.filter { it is KMutableProperty<*> }.map { it as KMutableProperty<*> }.first { name.equals(it.name) }

val <T : Any> KClass<T>.noArgsConstructor: KFunction<T>?
    get() = this.constructors.find { it.parameters.isEmpty() }


fun <T : Any> KClass<T>.matchingConstructorByTypes(vararg types:KClass<*>) =
    this.constructors.find { it.parameters.foldRight(true, { parm, value -> value and types.contains(parm.type.jvmErasure)}) }

fun <T : Any> KClass<T>.matchingConstructorByNames(vararg name:String) =
    this.constructors.find { it.parameters.foldRight(true, { parm, value -> value and name.contains(parm.name)}) }
