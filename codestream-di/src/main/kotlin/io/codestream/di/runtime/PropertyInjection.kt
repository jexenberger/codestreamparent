package io.codestream.di.runtime

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.KMutableProperty

class PropertyInjection(val id: ComponentId, val property: KMutableProperty<*>, val instance: Any) {

    fun run(ctx: Context) {

        val type = instance::class
        val callSite = DependencyTarget(id, type, property)
        val dependency = DependencyResolver.getDependency(callSite, ctx)
        dependency?.let {
            val value = it.resolve<Any>(callSite, ctx)
            if (value == null && !callSite.isNullable) {
                throw PropertyInjectionException(type,callSite.name,"property is not nullable, but nullable value resolved")
            }
            try {
                property.setter.call(instance, value)
            } catch (e:IllegalArgumentException) {
                throw PropertyInjectionException(type, callSite.name, "Unable to set property -> '${property.name}' -> ${e.message}")
            }
        }
    }

}