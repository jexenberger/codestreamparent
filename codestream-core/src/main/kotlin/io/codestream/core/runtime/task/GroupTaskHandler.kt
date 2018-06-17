package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.GroupTask
import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.tree.Branch
import io.codestream.core.runtime.tree.BranchProcessingDirective
import io.codestream.util.returnIfTrue

class GroupTaskHandler(private val taskId: TaskId,
                       parallel: Boolean,
                       val taskDef: GroupTaskDef) : Branch<StreamContext>(taskId.toString(), parallel) {


    val groupTask: GroupTask
        get() = workingCtx.get<GroupTask>(taskId) ?: throw ComponentFailedException(taskId.stringId, "not defined")

    private var workingCtx: StreamContext = StreamContext()

    override fun preTraversal(ctx: StreamContext): BranchProcessingDirective {
        return taskDef.condition(workingCtx.bindings)
                .returnIfTrue {
                    groupTask.before(taskDef, workingCtx.bindings)

                } ?: BranchProcessingDirective.abort
    }

    override fun postTraversal(ctx: StreamContext): BranchProcessingDirective {
        return groupTask.after(taskDef, ctx.bindings)
    }


    override fun onError(error: Exception, ctx: StreamContext) {
        groupTask.onError(error, taskDef, ctx.bindings)
    }

    override fun enterBranch(ctx: StreamContext) {
        workingCtx = ctx.subContext()
    }

    override fun exitBranch(ctx: StreamContext) {
        groupTask.onFinally(taskDef, ctx.bindings)
    }
}