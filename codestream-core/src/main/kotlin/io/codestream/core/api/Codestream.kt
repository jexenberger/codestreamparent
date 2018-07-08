package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.runtime.CodestreamRuntime
import io.codestream.core.runtime.StreamContext
import io.codestream.di.event.EventHandler
import java.io.File

abstract class Codestream {

    abstract val modules:Set<Pair<String, Version>>


    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback) : Map<String, Any?>
    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>) : Map<String, Any?>
    abstract fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback): Map<String, Any?>

    val eventHandlers = mutableSetOf<EventHandler<*>>()

    operator fun plusAssign(handler:EventHandler<*>) {
        eventHandlers += handler
    }

    companion object {
        @JvmStatic
        fun get(settings: CodestreamSettings): Codestream {
            return CodestreamRuntime(settings)
        }

        @JvmStatic
        fun get() : Codestream {
            return get(CodestreamSettings())
        }

        fun runContext() : RunContext {
            return StreamContext().bindings
        }
    }

}