package io.codestream.core.doc

import io.codestream.core.api.ModuleId

data class ModuleDoc(val module: ModuleId, val description:String, val tasks:Collection<TaskDoc>, val functions:Collection<FunctionDoc>) {
}