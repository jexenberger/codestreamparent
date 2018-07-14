package io.codestream.runtime.modules.system

import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.Directive

@Task(name = "foreach", description = "iterables over any iterable value")
class ForEach(
        @Parameter(description = "Item collection over which to iterate, works with any Array or Iterable type")
        var items: Iterator<*>,
        @Parameter(description = "Variable name of the current cipherText being iterator over, default is '__var'", default = "__var")
        var varName: String,
        @Parameter(description = "Variable which stores the working Iterator, default is '__iterator'", default = "__iterator")
        var iteratorVar: String

) : GroupTask {

    override fun before(ctx: RunContext): Directive {
        if (ctx.containsKey(iteratorVar)) {
            @Suppress("UNCHECKED_CAST")
            return setEvaluation(ctx[iteratorVar] as Iterator<Any>, ctx)
        }
        val iterator = items.iterator()
        ctx[iteratorVar] = iterator
        return setEvaluation(iterator, ctx)
    }

    private fun setEvaluation(it: Iterator<*>, ctx: RunContext): Directive {
        return if (it.hasNext()) {
            ctx[varName] = it.next()
            Directive.continueExecution
        } else {
            Directive.done
        }
    }

    override fun after(ctx: RunContext): Directive = Directive.again

    override fun onFinally(ctx: RunContext) {
    }
}