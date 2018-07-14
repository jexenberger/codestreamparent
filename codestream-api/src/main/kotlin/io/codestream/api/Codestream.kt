package io.codestream.api

import de.skuzzle.semantic.Version
import io.codestream.di.event.EventHandler
import io.codestream.doc.ModuleDoc
import java.io.File

abstract class Codestream {

    abstract val modules:Set<ModuleId>


    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback) : Map<String, Any?>
    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>) : Map<String, Any?>
    abstract fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback): Map<String, Any?>

    abstract fun moduleDoc(name:ModuleId) : ModuleDoc?
    abstract fun taskDoc(name:TaskType) : TaskType?

    val eventHandlers = mutableSetOf<EventHandler<*>>()


    fun modulesByName(name:String) : Set<ModuleId> = modules.filter { it.name.equals(name) }.toSet()

    operator fun plusAssign(handler:EventHandler<*>) {
        eventHandlers += handler
    }

}