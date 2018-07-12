package io.codestream.core.runtime.modules.system

import io.codestream.core.api.FunctionalTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
import io.codestream.util.Eval
import javax.script.Bindings
import java.io.File

@Task(name = "script", description = "Run a script either directly as text of from a file")
class Script(
        @Parameter(description = "Value to set from the result of the script", default = "__scriptOutput")
        override val outputVariable: String,
        @Parameter(description = "Script string or File path")
        val script:String,
        @Parameter(description = "Language of the script", default = "groovy")
        val language:Language) : FunctionalTask {

    enum class Language {
        groovy,
        javascript
    }

    override fun getResult(ctx: RunContext): Any? {
        val candidateFile = File(script)
        val executableScript = if (candidateFile.isFile) candidateFile.readText() else script
        val bindings = ctx as Bindings
        return Eval.eval(executableScript, bindings, Eval.engineByName(language.name))
    }
}