package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.codestream.api.Codestream
import io.codestream.api.CodestreamSettings
import io.codestream.di.api.*
import io.codestream.util.Eval
import io.codestream.util.OS
import io.codestream.util.ifTrue
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
import io.codestream.util.io.console.decorate
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

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

    val commandlet: Future<Commandlet>

    init {
        //force parameters to be loaded
        task
        inputParms
        if (threads > 1) {
            executorService.submit(Callable<Commandlet> { run { Eval.eval("1==1") } })
        }
        commandlet = executorService.submit(Callable<Commandlet> { run { startContainer(task, inputParms) } })
    }


    private fun startContainer(task: String, parmeters: MutableList<Pair<String, String?>>): Commandlet {
        val parms = parmeters.toTypedArray().toMap()

        setValue("yaml.module.path", File("${OS.os().homeDir}/.cs/_modules"))
        setValue("enable.debug", true)
        setValue("task.ref", this.task)
        setValue("input.parameters", parms)

        addType<CodestreamSettings>(CodestreamSettings::class) into this
        addInstance(args) withId StringId("args") into this
        addInstance(setOf(ConsoleHandler(true))) withId StringId("eventHandlers") into this
        addInstance(executorService) withId TypeId(ExecutorService::class) into this

        commands.forEach { type, cmd ->
            addType<Commandlet>(cmd) withId StringId(type) into this
        }
        addType<CodestreamRuntimeFactory>(CodestreamRuntimeFactory::class) withId TypeId(Codestream::class) into this
        return get<Commandlet>(command!!)!!
    }

    fun run() {
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
                commandlet.cancel(true)
                return
            }
            commandlet.get().run()
        } finally {
            executorService.shutdownNow()
        }

    }

    companion object {
        val commands = mapOf(
                "run" to RunTaskCommandlet::class,
                "help" to HelpCommandlet::class
        )
    }

}