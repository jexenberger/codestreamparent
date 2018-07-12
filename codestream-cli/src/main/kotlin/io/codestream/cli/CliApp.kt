package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.codestream.core.api.Codestream
import io.codestream.core.api.CodestreamSettings
import io.codestream.di.api.*
import io.codestream.util.OS
import io.codestream.util.ifTrue
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
import io.codestream.util.io.console.decorate
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CliApp(val args: ArgParser) : ApplicationContext() {

    val threads = Runtime.getRuntime().availableProcessors()
    val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    val command: String? by args.positional(name = "COMMAND", help = "command to execute")
    val task: String by args.positional(name = "COMMAND_OPTION", help = "Task file or task in format <module(@version)::task>").default("")
    val inputParms by args.adding("-I", "--input", help = "input parameter (format: [name]=[items]") {
        val parts = this.split("=")
        if (parts.size > 1) {
            Pair(parts[0], parts[1])
        } else {
            Pair(this, null)
        }
    }

    private fun startContainer(): Boolean {


        val parms = inputParms.toTypedArray().toMap()

        setValue("yaml.module.path", File("${OS.os().homeDir}/.cs/_modules"))
        setValue("enable.debug", true)
        setValue("task.ref", task)
        setValue("input.parameters", parms)

        addType<CodestreamSettings>(CodestreamSettings::class) into this
        addInstance(args) withId StringId("args") into this
        addInstance(setOf(ConsoleHandler(true))) withId StringId("eventHandlers") into this
        addInstance(executorService) withId TypeId(ExecutorService::class) into this

        commands.forEach { type, cmd ->
            addType<Commandlet>(cmd) withId StringId(type) into this
        }
        addType<CodestreamRuntimeFactory>(CodestreamRuntimeFactory::class) withId TypeId(Codestream::class) into this
        return true
    }

    fun run() {
        val startup = executorService.submit(Callable<Boolean> { run { startContainer() }})
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
            startup.cancel(true)
            return
        }
        Console.display(bold("Running '${command} ${task.isNotBlank().ifTrue { " -> $task" } ?: ""}..."))
                .newLine()
                .newLine()
        if (!startup.get()) {
            return
        }
        val command = get<Commandlet>(command!!)!!
        println("ready!!")
        command.run()
    }

    companion object {
        val commands = mapOf(
                "run" to RunTaskCommandlet::class,
                "help" to HelpCommandlet::class
        )
    }

}