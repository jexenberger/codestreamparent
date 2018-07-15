package io.codestream.runtime.modules.system

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.util.io.console.Console
import io.codestream.util.system

@Task(name = "shell", description = "Runs a command against the system shell, 'bin/sh' in *nix and cmd.exe on windows")
class Shell(
        @Parameter(description = "command to run")
        val cmd:String,
        @Parameter(description = "name of the variable to set in the current context", default = "__outputVar")
        override val outputVariable: String,
        @Parameter(description = "name of the variable to set the return code from the script", default = "__resultCodeVar")
        val resultCodeVariable: String,
        @Parameter(description = "directory to run the command in, default is pwd")
        val dir:String = system.pwd,
        @Parameter(description = "Echo output to the screen, default it true")
        val echo:Boolean = true
) : FunctionalTask {
    override fun getResult(ctx: RunContext): Any? {

        val buffer = mutableListOf<String>()
        val result = system.shell(cmd,dir) {
            buffer += it
            if (echo) {
                Console.display(it).newLine()
            }
        }
        ctx[resultCodeVariable] = result
        return buffer.joinToString(system.newLine)
    }
}