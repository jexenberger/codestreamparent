package io.codestream.doc

import io.codestream.api.ModuleId

data class ModuleDoc(val module: ModuleId, val description:String, val tasks:Collection<TaskDoc>, val functions:Collection<FunctionDoc>) {

    fun taskDoc(name:String) = tasks.find { it.name.equals(name) }

}