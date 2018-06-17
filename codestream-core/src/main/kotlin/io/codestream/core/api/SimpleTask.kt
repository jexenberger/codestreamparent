package io.codestream.core.api

import io.codestream.core.metamodel.TaskDef

interface SimpleTask : Task {

    fun run(def:TaskDef, ctx:TaskContext)

}