package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.codestream.core.api.Codestream
import io.codestream.core.api.CodestreamSettings
import io.codestream.di.api.*
import io.codestream.util.OS
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CliApp(val args: ArgParser) : ApplicationContext() {

    val threads = Runtime.getRuntime().availableProcessors()

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

    init {

        val parms = inputParms.toTypedArray().toMap()

        setValue("yaml.module.path", File("${OS.os().homeDir}/.cs/_modules"))
        setValue("enable.debug", true)
        setValue("task.ref", task)
        setValue("input.parameters", parms)

        addType<CodestreamSettings>(CodestreamSettings::class) into this
        addInstance(args) withId StringId("args") into this
        addInstance(setOf(ConsoleHandler(true))) withId StringId("eventHandlers") into this
        addInstance(Executors.newFixedThreadPool(threads)) withId TypeId(ExecutorService::class) into this

        addType<Commandlet>(RunTaskCommandlet::class) withId StringId("run") into this
        addType<CodestreamRuntimeFactory>(CodestreamRuntimeFactory::class) withId TypeId(Codestream::class) into this
        addType<CodestreamRuntimeFactory>(CodestreamRuntimeFactory::class) withId TypeId(Codestream::class) into this

    }

    fun run() {
        val command = get<Commandlet>(command!!)!!
        command.run()
    }

}