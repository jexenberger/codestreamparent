package io.codestream.di.runtime

import io.codestream.di.annotation.Eval
import io.codestream.di.api.AnnotationDependency
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget

class EvalDependency : AnnotationDependency<Eval>(Eval::class, true, true) {
    override fun <T> resolve(annotation: Eval, target: DependencyTarget, ctx: Context): T? {
        val engine = io.codestream.util.script.Eval.engineByName(annotation.engine)
        return io.codestream.util.script.Eval.eval(annotation.value, ctx.bindings, engine)
    }
}