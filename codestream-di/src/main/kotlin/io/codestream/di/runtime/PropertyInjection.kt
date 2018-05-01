package io.codestream.di.runtime

import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.KMutableProperty

class PropertyInjection(val property: KMutableProperty<*>, val instance: Any) {

    fun run(ctx: Context) {

        val callSite = DependencyTarget(property)
        val dependency = DependencyResolver.getDependency(callSite, ctx)
        dependency?.let {
            val value = it.resolve<Any>(callSite, ctx)
            property.setter.call(instance, value)
        }
    }

}