package io.codestream.cli

import io.codestream.api.Codestream
import io.codestream.api.ParameterCallback
import io.codestream.api.TaskType
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Value
import io.codestream.util.ifTrue
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
import io.codestream.util.io.console.decorate
import io.codestream.util.io.console.warn
import io.codestream.util.stackDump
import java.io.File

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
                runFile()
            } else {
                runTask()
            }

        } catch (e: Exception) {
            displayError(e)
        }
    }

    private fun runTask() {
        val taskType = TaskType.fromString(task)
        val result = codestream.runTask(
                module = taskType.module,
                task = taskType.name,
                parameters = inputParameters,
                callback = this
        )
        displayOutput(result)
    }

    private fun displayOutput(result: Map<String, Any?>) {
        if (debug) {
            result.entries.forEach { (k, v)  ->
                Console.display(bold(k
                ).padEnd(15)).display(":").display(v.toString()).newLine()
            }
        }
    }

    private fun runFile() {
        val file = File(task)
        if (!file.exists() || file.isDirectory) {
            Console.display(warn("$task is not a file")).newLine()
            return
        }
        codestream.runTask(file, inputParameters, this)
    }

    private fun displayError(e: Exception) {
        Console.display(decorate("ERROR:", Console.ANSI_RED, Console.REVERSED))
                .space()
                .display(bold(e.message!!))
                .newLine()
        if (this.debug) {
            Console.display(decorate(e.stackDump, Console.ANSI_RED, Console.REVERSED)).newLine()
        }
    }


    override fun capture(descriptor: ParameterDescriptor): Any? {
        val prompt = if (descriptor.required) "Required: #" else "#"
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
                .display(bold(prompt))
                .space()

        val readVal = Console.get()
        val ret = readVal.isBlank().ifTrue { descriptor.default } ?: readVal
        return if (descriptor.required && ret.isBlank()) {
            capture(descriptor)
        } else {
            ret
        }
    }
}