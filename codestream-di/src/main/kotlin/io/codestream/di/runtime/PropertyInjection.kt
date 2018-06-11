package io.codestream.di.runtime

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.KMutableProperty

class PropertyInjection(val id: ComponentId, val property: KMutableProperty<*>, val instance: Any) {

    fun run(ctx: Context) {

        val callSite = DependencyTarget(id, instance::class, property)
        val dependency = DependencyResolver.getDependency(callSite, ctx)
        dependency?.let {
            val value = it.resolve<Any>(callSite, ctx)
            property.setter.call(instance, value)
        }
    }

}