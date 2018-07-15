package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.codestream.api.Codestream
import io.codestream.api.CodestreamSettings
import io.codestream.di.api.*
import io.codestream.util.Eval
import io.codestream.util.OS
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.decorate
import io.codestream.util.system
import sun.misc.Unsafe
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

    init {
        disableIllegalAccessWarnings()
    }


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
        OS.os().optimizedExecutor.submit(Callable<Commandlet> { run { Eval.eval("1==1") } })
        val commandlet = OS.os().optimizedExecutor.submit(Callable<Commandlet> { run { startContainer(inputParms) } })

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

            } else {
                commandlet.get().run()
            }
        } finally {
            //OS.os().optimizedExecutor.shutdownNow()
        }

    }

    companion object {
        val commands = mapOf(
                "run" to RunTaskCommandlet::class,
                "help" to HelpCommandlet::class
        )
    }


    fun disableIllegalAccessWarnings() {
        try {
            val theUnsafe = Unsafe::class.java!!.getDeclaredField("theUnsafe")
            theUnsafe.setAccessible(true)
            val u = theUnsafe.get(null) as Unsafe

            val cls = Class.forName("jdk.internal.module.IllegalAccessLogger")
            val logger = cls.getDeclaredField("logger")
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null)
        } catch (e: Exception) {
            // ignore
        }

    }

}