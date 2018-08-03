/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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