package io.codestream.di.api

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class DependencyTarget(val property: KMutableProperty<*>?,
                            val parameter: KParameter?) {

    constructor(property: KMutableProperty<*>) : this(property, null)
    constructor(parameter: KParameter) : this(null, parameter)

    init {
        if (property != null && parameter != null) {
            throw IllegalStateException("an either be property or parameter but not both")
        }
        if (property == null && parameter == null) {
            throw IllegalStateException("must define either parameter to property")
        }
    }

    val targetType: KClass<*> get() = perform({ it.returnType.jvmErasure }, { it.type.jvmErasure })

    val name: String get() = perform({ it.name }, { it.name!! })

    val annotatedElement: KAnnotatedElement get() = perform({ it }, { it })

    fun supports(dependency: Dependency): Boolean {
        return perform({ dependency.supports(it) }, { dependency.supports(it) })
    }

    private fun <T> perform(property: (p: KMutableProperty<*>) -> T, parameter: (p: KParameter) -> T): T {
        if (this.property != null) {
            return property(this.property!!)
        }
        if (this.parameter != null) {
            return parameter(this.parameter!!)
        }
        //should never ctx here
        throw IllegalStateException("an either be property or parameter but not both")
    }


}