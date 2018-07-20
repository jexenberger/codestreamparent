package io.codestream.cli

import io.codestream.api.Codestream
import io.codestream.api.ParameterCallback
import io.codestream.api.TaskType
import io.codestream.api.Type
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
                Console.display(bold(k)
                        .padEnd(20))
                        .display(":")
                        .display(v.toString())
                        .newLine()
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
        val prompt = if (descriptor.required) "Required: :" else ":"
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

        val readVal = captureValue(descriptor) ?: descriptor.defaultValue
        return if (descriptor.required && readVal == null) {
            capture(descriptor)
        } else {
            readVal
        }
    }

    fun captureSecret() : Any? {
        do {
            val a = Console.getSecret()
            if (a == null) {
                return null
            }
            Console.display("Re-enter :").newLine()
            val b = Console.getSecret()
            if (Objects.equals(a, b)) {
                return a
            }
        } while (0==0)

    }

    fun captureValue(type:ParameterDescriptor) : Any? {
        val typeMapping = mapOf<Type, ()-> Any?>(
                Type.keyValue to { captureMap() },
                Type.secret to { captureSecret() }
        )
        return typeMapping[type.type]?.let { it() } ?: Console.getNullForBlank()
    }

    fun captureMap(): Map<String, Any?>? {
        val keyValues = mutableListOf<Pair<String, Any?>>()
        var keyValue:Pair<String, Any?>? = null
        do {
            keyValue = captureKey()
            keyValue?.let { keyValues.add(it) }
        } while (keyValue == null)
        return keyValues.toMap().takeUnless { it.isEmpty() }
    }

    fun captureKey(): Pair<String, Any?>? {
        Console.newLine().display(bold("key")).space().display("(type !!! to quit) : ")
        val key = Console.get()
        if (key.equals("!!!")) {
            return null
        }
        Console.display(bold("Value")).display("(type !!! to quit) : ")
        val value = Console.get()
        if (value.equals("!!!")) {
            return null
        }
        return key to value

    }


}