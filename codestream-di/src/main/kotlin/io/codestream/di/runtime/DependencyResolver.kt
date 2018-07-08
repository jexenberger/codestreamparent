package io.codestream.di.runtime

import io.codestream.di.api.Context
import io.codestream.di.api.Dependency
import io.codestream.di.api.DependencyTarget
import java.util.*


object DependencyResolver {


    private val RESOLVERS: Stack<Dependency> = Stack()

    init {
        RESOLVERS.push(InjectionDependency())
        RESOLVERS.push(TypedDependency())
        RESOLVERS.push(ValueDependency())
        RESOLVERS.push(EvalDependency())
    }

    fun getDependency(call: DependencyTarget, ctx: Context) : Dependency? {
        return RESOLVERS.reversed().find {
            call.supports(it)
        }
    }

    fun add(dependency: Dependency) : DependencyResolver {
        RESOLVERS.push(dependency)
        return this
    }

}