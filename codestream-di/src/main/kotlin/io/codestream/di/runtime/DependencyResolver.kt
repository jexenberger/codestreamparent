package io.codestream.di.runtime

import io.codestream.di.api.Context
import io.codestream.di.api.Dependency
import io.codestream.di.api.DependencyTarget


object DependencyResolver {


    private val RESOLVERS: MutableSet<Dependency> = linkedSetOf()

    init {
        RESOLVERS.add(InjectionDependency())
        RESOLVERS.add(ValueDependency())
        RESOLVERS.add(TypedDependency())
        RESOLVERS.add(EvalDependency())
    }

    fun getDependency(call: DependencyTarget, ctx: Context) : Dependency? {
        return RESOLVERS.find {
            call.supports(it)
        }
    }

    fun add(dependency: Dependency) : DependencyResolver {
        RESOLVERS.add(dependency)
        return this
    }

}