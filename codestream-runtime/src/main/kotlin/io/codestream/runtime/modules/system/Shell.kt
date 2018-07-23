package io.codestream.runtime.modules.system

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.util.io.console.Console
import io.codestream.util.system

@Task(name = "shell", description = "Runs a command against the system shell, 'bin/sh' in *nix and cmd.exe on windows", returnDescription = "returns a keyValue with return code mapped to 'result' and the script output mapped to 'output'")
class Shell(
        @Parameter(description = "command to evaluate")
        val cmd:String,
        @Parameter(description = "directory to evaluate the command in, default is pwd")
        val dir:String = system.pwd,
        @Parameter(description = "Echo output to the screen, default it true")
        val echo:Boolean = true
) : FunctionalTask<Map<String, Any?>> {
    override fun evaluate(ctx: RunContext): Map<String, Any?>? {

        val buffer = mutableListOf<String>()
        val result = system.shell(cmd,dir) {
            buffer += it
            if (echo) {
                Console.display(it).newLine()
            }
        }
        val toString = buffer.joinToString(system.newLine)
        return mapOf(
                "result" to result,
                "output" to toString
        )
    }
}