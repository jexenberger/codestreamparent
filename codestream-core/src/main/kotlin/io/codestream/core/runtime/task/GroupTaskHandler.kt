package io.codestream.core.runtime.task

import io.codestream.core.api.ComponentFailedException
import io.codestream.core.api.GroupTask
import io.codestream.core.api.TaskError
import io.codestream.core.api.events.*
import io.codestream.core.api.metamodel.GroupTaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.api.TaskId
import io.codestream.core.runtime.container.TaskScope
import io.codestream.core.runtime.container.TaskScopeId
import io.codestream.core.runtime.tree.Branch
import io.codestream.core.runtime.tree.BranchProcessingDirective
import io.codestream.util.Timer
import io.codestream.util.ifTrue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GroupTaskHandler(private val taskId: TaskId,
                       parallel: Boolean,
                       val taskDef: GroupTaskDef,
                       executorService: ExecutorService = Executors.newFixedThreadPool(2))
    : Branch<StreamContext>(taskId.toString(), parallel, executorService) {


    private val timer: Timer by lazy { Timer() }

    private fun getTask(ctx: StreamContext) = ctx.get<GroupTask>(taskId)
            ?: throw ComponentFailedException(taskId.stringId, "not defined")



    override fun preTraversal(ctx: StreamContext): BranchProcessingDirective {
        return taskDef.condition(ctx.bindings)
                .ifTrue {
                    //init the timer
                    timer
                    val result = getTask(ctx).before(ctx.bindings)
                    fireEvent(ctx, BeforeTaskEvent(taskId, result.name, result.state), result)
                    result

                } ?: fireEvent(ctx, TaskSkippedEvent(taskId, "Task condition evaluated to false"), BranchProcessingDirective.abort)
    }

    private fun fireEvent(ctx:StreamContext, event: CodestreamEvent, directive:BranchProcessingDirective) : BranchProcessingDirective {
        ctx.events.publish(event)
        return directive
    }

    override fun postTraversal(ctx: StreamContext): BranchProcessingDirective {
        val result = getTask(ctx).after(ctx.bindings)
        return fireEvent(ctx, AfterTaskEvent(taskId,result.name, result.state, timer.currentTimeTaken), result)
    }


    override fun onError(error: Exception, ctx: StreamContext) {
        val taskError = TaskError(error, ctx.bindings)
        ctx.bindings["_error_"] = taskError
        getTask(ctx).onError(taskError,  ctx.bindings)
        fireEvent(ctx, TaskErrorEvent(taskId, taskError), BranchProcessingDirective.done)
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