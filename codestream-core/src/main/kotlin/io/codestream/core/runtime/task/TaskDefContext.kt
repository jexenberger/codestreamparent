package io.codestream.core.runtime.task

import io.codestream.core.runtime.metamodel.TaskDef

object TaskDefContext {

    private val def: ThreadLocal<TaskDef> = ThreadLocal()

    var defn:TaskDef
        get() = def.get() ?: throw IllegalStateException("Task context not set")
        set(value) = def.set(value)

    fun clear() {
        def.remove()
    }

}