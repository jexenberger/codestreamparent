package io.codestream.core.api

import io.codestream.core.metamodel.TaskDef
import io.codestream.core.runtime.TaskId

interface Task {

    fun run(defn:TaskDef, ctx:TaskContext)
}