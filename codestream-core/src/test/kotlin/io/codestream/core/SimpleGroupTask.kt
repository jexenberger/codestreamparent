package io.codestream.core

import io.codestream.core.api.GroupTask
import io.codestream.core.api.TaskContext
import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.runtime.tree.BranchProcessingDirective

class SimpleGroupTask : GroupTask {

    var error = false
    var before = false
    var after = false
    var onFinally = false

    override fun before(defn: GroupTaskDef, ctx: TaskContext): BranchProcessingDirective {
        before = true
        return BranchProcessingDirective.continueExecution
    }

    override fun after(defn: GroupTaskDef, ctx: TaskContext): BranchProcessingDirective {
        after = true
        return BranchProcessingDirective.done
    }

    override fun onError(error: Exception, defn: GroupTaskDef, ctx: TaskContext) {
        this.error = true
    }

    override fun onFinally(defn: GroupTaskDef, ctx: TaskContext) {
        onFinally = true
    }


}