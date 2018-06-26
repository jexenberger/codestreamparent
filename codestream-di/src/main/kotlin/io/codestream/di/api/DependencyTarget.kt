package io.codestream.di.api

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class DependencyTarget(val id:ComponentId,
                            val owner: KClass<*>,
                            val property: KMutableProperty<*>?,
                            val parameter: KParameter?) {

    constructor(id: ComponentId, owner: KClass<*>, property: KMutableProperty<*>) : this(id, owner, property, null)
    constructor(id: ComponentId, owner: KClass<*>, parameter: KParameter) : this(id, owner, null, parameter)

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

    val isOptional:Boolean get() {
        if (this.property != null) {
            return this.property.returnType.isMarkedNullable
        }
        if (this.parameter != null) {
            return this.parameter.isOptional
        }
        //should never get here
        throw IllegalStateException("an either be property or parameter but not both")
    }

    fun supports(dependency: Dependency): Boolean {
        return perform({ dependency.supports(it) }, { dependency.supports(it) })
    }

    private fun <T> perform(property: (p: KMutableProperty<*>) -> T, parameter: (p: KParameter) -> T): T {
        if (this.property != null) {
            return property(this.property)
        }
        if (this.parameter != null) {
            return parameter(this.parameter)
        }
        //should never get here
        throw IllegalStateException("an either be property or parameter but not both")
    }




}