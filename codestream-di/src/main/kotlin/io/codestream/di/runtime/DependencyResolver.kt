package io.codestream.di.runtime

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Value
import io.codestream.di.api.Context
import io.codestream.di.api.Dependency
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses


object DependencyResolver {


    private val RESOLVERS: MutableSet<Dependency> = linkedSetOf()

    init {
        RESOLVERS.add(InjectionDependency())
        RESOLVERS.add(ValueDependency())
        RESOLVERS.add(TypedDependency())
    }

    fun getDependency(call: DependencyTarget, ctx: Context) : Dependency? {
        return RESOLVERS.find {
            call.supports(it)
        }
    }

}