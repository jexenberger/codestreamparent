package io.codestream.runtime.task

import io.codestream.api.TaskError
import io.codestream.api.TaskId
import io.codestream.api.events.*
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.StreamContext
import io.codestream.runtime.container.TaskScope
import io.codestream.runtime.container.TaskScopeId
import io.codestream.runtime.tree.Leaf
import io.codestream.runtime.tree.Node
import io.codestream.runtime.tree.NodeState

abstract class BaseLeafTaskHandler(
        val taskId: TaskId, val taskDef: TaskDef
) : Leaf<StreamContext>(taskId.id) {


    override fun visitSkipped(state: NodeState, leaf: Node<StreamContext>, ctx: StreamContext) {
        ctx.events.publish(TaskSkippedEvent(taskId, "Task condition evaluated to false"))
    }

    override fun visitBeforeLeaf(leaf: Node<StreamContext>, ctx: StreamContext): Boolean {
        ctx.events.publish(BeforeTaskEvent(taskId, "running", TaskState.running))
        val condition = taskDef.condition(ctx.bindings)
        return condition
    }

    override fun visitWhenError(error: Exception, leaf: Node<StreamContext>, ctx: StreamContext): Exception {
        ctx.events.publish(TaskErrorEvent(taskId, TaskError(taskId, error, ctx.bindings)))
        return error
    }

    override fun visitAfterLeaf(state: NodeState, leaf: Node<StreamContext>, ctx: StreamContext, timeTaken: Pair<Long, Unit>) {
        ctx.events.publish(AfterTaskEvent(taskId, "completed", TaskState.completed, timeTaken.first))
    }

    override fun handle(ctx: StreamContext) {
        try {
            TaskDefContext.defn = taskDef
            run(ctx)
        } catch (e: Exception) {
            throw e
        } finally {
            TaskDefContext.clear()
            TaskScope.release(TaskScopeId(ctx, taskId))
        }
    }

    abstract fun run(ctx: StreamContext)


}