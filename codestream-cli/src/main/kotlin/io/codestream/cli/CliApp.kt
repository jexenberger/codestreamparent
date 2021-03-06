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

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.codestream.api.Codestream
import io.codestream.api.CodestreamSettings
import io.codestream.di.api.*
import io.codestream.util.script.Eval
import io.codestream.util.OS
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.decorate
import java.util.concurrent.Callable


class CliApp(val args: ArgParser) : ApplicationContext() {

    val command: String? by args.positional(name = "COMMAND", help = "command to execute, can be one of :${CliApp.commands.keys.map { "'$it'" }.joinToString(", ")}")
    val task: String by args.positional(name = "COMMAND_OPTION", help = "Parameter for command option, type 'cs help' for individual commands").default("")
    val inputParms by args.adding("-I", "--input", help = "input parameter (format: [name]=[items]") {
        val parts = this.split("=")
        if (parts.size > 1) {
            Pair(parts[0], parts[1])
        } else {
            Pair(this, null)
        }
    }
    val debug by args.flagging("-D", "--debug", help = "Run with debug output").default(false)


    private fun startContainer(parmeters: MutableList<Pair<String, String?>>): Commandlet {
        val parms = parmeters.toTypedArray().toMap()

        setValue("enable.debug", debug)
        setValue("task.ref", this.task)
        setValue("input.parameters", parms)

        addInstance(CodestreamSettings()) withId TypeId(CodestreamSettings::class) into this
        addInstance(args) withId StringId("args") into this
        addInstance(setOf(ConsoleHandler(true))) withId StringId("eventHandlers") into this

        commands.forEach { type, cmd ->
            addType<Commandlet>(cmd) withId StringId(type) into this
        }
        addType<CodestreamRuntimeFactory>(CodestreamRuntimeFactory::class) withId TypeId(Codestream::class) into this
        return get<Commandlet>(command!!)!!
    }

    fun run() = mainBody("cs") {
        task
        inputParms
        val commandlet = startContainer(inputParms)
        try {
            if (!CliApp.commands.containsKey(command)) {
                Console.display(
                        decorate(
                                "'$command' is not a recognised command, must be one of:",
                                Console.BOLD,
                                Console.ANSI_RED)).newLine()
                CliApp.commands.keys.forEach {
                    Console.tab()
                            .display(decorate("- $it", Console.BOLD, Console.ANSI_RED))
                            .newLine()
                }

            } else {
                commandlet.run()
            }
        } finally {
            OS.os().optimizedExecutor.shutdownNow()
        }

    }

    companion object {
        val commands = mapOf(
                "run" to RunTaskCommandlet::class,
                "help" to HelpCommandlet::class
        )
    }
}