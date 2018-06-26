package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.GroupTask
import io.codestream.core.api.TaskError
import io.codestream.core.runtime.metamodel.GroupTaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.container.TaskScope
import io.codestream.core.runtime.container.TaskScopeId
import io.codestream.core.runtime.tree.Branch
import io.codestream.core.runtime.tree.BranchProcessingDirective
import io.codestream.util.ifTrue

class GroupTaskHandler(private val taskId: TaskId,
                       parallel: Boolean,
                       val taskDef: GroupTaskDef) : Branch<StreamContext>(taskId.toString(), parallel) {


    private fun getTask(ctx:StreamContext) = ctx.get<GroupTask>(taskId) ?: throw ComponentFailedException(taskId.stringId, "not defined")

    override fun preTraversal(ctx: StreamContext): BranchProcessingDirective {
        return taskDef.condition(ctx.bindings)
                .ifTrue {
                    getTask(ctx).before(ctx.bindings)

                } ?: BranchProcessingDirective.abort
    }

    override fun postTraversal(ctx: StreamContext): BranchProcessingDirective {
        return getTask(ctx).after(ctx.bindings)
    }


    override fun onError(error: TaskError, ctx: StreamContext) {
        ctx.bindings["_error_"] = error
        getTask(ctx).onError(error,  ctx.bindings)
    }

    override fun enterBranch(ctx: StreamContext) {
    }

    override fun exitBranch(ctx: StreamContext) {
        try {
            getTask(ctx).onFinally(ctx.bindings)
        } finally {
            TaskScope.release(TaskScopeId(ctx, taskId))
        }
    }

    override fun runChildren(ctx: StreamContext) {
        val childCtx = ctx.subContext()
        super.runChildren(childCtx)
    }

    override fun toString(): String {
        return "GroupTask -> $taskId)"
    }
}