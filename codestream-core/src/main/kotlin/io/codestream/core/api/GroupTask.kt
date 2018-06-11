package io.codestream.core.api

import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.runtime.tree.BranchProcessingDirective

interface GroupTask : Task {

    fun before(defn: GroupTaskDef, ctx: TaskContext): BranchProcessingDirective

    fun after(defn: GroupTaskDef, ctx: TaskContext): BranchProcessingDirective

    fun onError(error:Exception, defn: GroupTaskDef, ctx:TaskContext)

}