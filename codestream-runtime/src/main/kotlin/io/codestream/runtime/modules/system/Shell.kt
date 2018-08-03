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