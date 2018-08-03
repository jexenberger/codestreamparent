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