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

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.services.Language
import io.codestream.util.script.Eval
import javax.script.Bindings
import java.io.File

@Task(name = "script", description = "Run a script either directly as text of from a file", returnDescription = "Result of the script")
class Script(
        @Parameter(description = "Script string or File path")
        val script:String,
        @Parameter(description = "Language of the script", default = "mvel")
        val language: Language = Language.javascript) : FunctionalTask<Any?> {

    override fun evaluate(ctx: RunContext): Any? {
        val candidateFile = File(script)
        val executableScript = if (candidateFile.isFile) candidateFile.readText() else script
        val bindings = ctx as Bindings
        return Eval.eval(executableScript, bindings, Eval.engineByName(language.name))
    }
}