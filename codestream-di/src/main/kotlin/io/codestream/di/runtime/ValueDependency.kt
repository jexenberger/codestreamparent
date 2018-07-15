package io.codestream.di.runtime

import io.codestream.di.annotation.Value
import io.codestream.di.api.AnnotationDependency
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget

class ValueDependency : AnnotationDependency<Value>(Value::class, true, true) {

    override fun <T> resolve(annotation: Value, target: DependencyTarget, ctx: Context): T? {
        return ctx.value(annotation.value) ?: throw UnsatisfiedDependencyInjection(target.name, "unable to resolve ${annotation.value}")
    }

}