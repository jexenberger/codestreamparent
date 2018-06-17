package io.codestream.core.runtime.yaml

import io.codestream.core.api.*
import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.tree.BranchProcessingDirective
import io.codestream.di.api.Context

class YamlTask(val taskId: TaskId, val onError: TaskId?, val onFinally: TaskId?) : GroupTask {

    override fun before(defn: GroupTaskDef, ctx: TaskContext) = BranchProcessingDirective.continueExecution

    override fun after(defn: GroupTaskDef, ctx: TaskContext) = BranchProcessingDirective.done

    override fun onError(error: Exception, defn: GroupTaskDef, ctx: TaskContext) {
        runTaskHandler(ctx, defn, onError)
    }

    private fun runTaskHandler(ctx: TaskContext, defn: GroupTaskDef, handlerTask: TaskId?) {
        handlerTask?.let { theTask ->
            val context = ctx["_ctx"] as Context?
                    ?: throw ComponentFailedException(taskId.id, "context not loaded in Task Context")
            val task = context.get<SimpleTask>(theTask)
                    ?: throw ComponentFailedException(taskId.id, "is defined as error task but not resolved")
            try {
                task.run(defn, ctx)
            } catch (e: Exception) {
            }
        }
    }

    override fun onFinally(defn: GroupTaskDef, ctx: TaskContext) {
        runTaskHandler(ctx, defn, onFinally)
    }

}