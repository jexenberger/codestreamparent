package io.codestream.runtime.modules.system

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.services.Language
import io.codestream.util.Eval
import javax.script.Bindings
import java.io.File

@Task(name = "script", description = "Run a script either directly as text of from a file")
class Script(
        @Parameter(description = "Script string or File path")
        val script:String,
        @Parameter(description = "Language of the script", default = "groovy")
        val language: Language = Language.groovy) : FunctionalTask<Any?> {

    override fun evaluate(ctx: RunContext): Any? {
        val candidateFile = File(script)
        val executableScript = if (candidateFile.isFile) candidateFile.readText() else script
        val bindings = ctx as Bindings
        return Eval.eval(executableScript, bindings, Eval.engineByName(language.name))
    }
}