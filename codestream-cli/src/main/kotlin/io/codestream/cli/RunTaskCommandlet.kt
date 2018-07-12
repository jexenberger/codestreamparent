package io.codestream.cli

import de.skuzzle.semantic.Version
import io.codestream.core.api.Codestream
import io.codestream.core.api.CodestreamModule.Companion.defaultVersion
import io.codestream.core.api.ModuleId
import io.codestream.core.api.ParameterCallback
import io.codestream.core.api.descriptor.ParameterDescriptor
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Value
import io.codestream.util.ifTrue
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
import io.codestream.util.io.console.decorate
import io.codestream.util.io.console.warn
import java.io.File
import java.util.*

class RunTaskCommandlet(
        @Inject
        val codestream: Codestream,
        @Value("task.ref")
        val task: String,
        @Value("input.parameters")
        val inputParameters: Map<String, Any?>,
        @Value("enable.debug")
        val debug: Boolean
) : Commandlet, ParameterCallback {
    override fun run() {

        try {

            if (task.endsWith(".yaml")) {
                if (runFile()) return
            } else {
                val parts = task.split("::")
                if (parts.size != 2) {
                    Console.display(warn("$task must be in format <module(@version)::task"))
                    return
                }
                val module = parts[0].let {
                    val modParts = it.split("@")
                    if (modParts.size == 2) {
                        try {
                            ModuleId(modParts[0], Version.parseVersion(modParts[1]))
                        } catch (e: Version.VersionFormatException) {
                            Console.display(warn("$task must be in format <module(@version)::task"))
                            return
                        }
                    } else {
                        ModuleId(it, defaultVersion)
                    }
                }
                val task = parts[1]

                codestream.runTask(
                        module = module,
                        task = task,
                        parameters = inputParameters,
                        callback = this
                )
            }

        } catch (e: Exception) {
            displayError(e)
        }
    }

    private fun runFile(): Boolean {
        val file = File(task)
        if (!file.exists() || file.isDirectory) {
            Console.display(warn("$task is not a file"))
            return true
        }
        codestream.runTask(file, inputParameters, this)
        return false
    }

    private fun displayError(e: Exception) {
        Console.display(decorate("ERROR:", Console.ANSI_RED, Console.REVERSED))
                .space()
                .display(bold(e.message!!))
                .newLine()
        if (this.debug) {
            e.printStackTrace()
        }
    }


    override fun capture(descriptor: ParameterDescriptor): Any? {
        val prompt = if (descriptor.required) bold("Required: #") else "#"
        val default = if (descriptor.default != null) "default value: '${descriptor.default}'" else ""
        Console.display("Enter")
                .space()
                .display(bold(descriptor.name))
                .space()
                .display("(")
                .display(descriptor.description)
                .display(")")
                .space()
                .display(default)
                .newLine()
                .display(prompt)
                .space()

        val ret = Console.get()
        return if (descriptor.required && descriptor.default.isNullOrBlank() && ret.isBlank()) {
            capture(descriptor)
        } else {
            null
        }
    }
}