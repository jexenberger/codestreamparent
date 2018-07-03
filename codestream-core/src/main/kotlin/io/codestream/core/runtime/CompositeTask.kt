package io.codestream.core.runtime

import io.codestream.core.api.*
import io.codestream.core.runtime.task.GroupTaskHandler
import io.codestream.core.runtime.task.SimpleTaskHandler
import io.codestream.core.runtime.task.TaskDefContext
import io.codestream.core.runtime.tree.Branch
import io.codestream.core.runtime.tree.BranchProcessingDirective
import io.codestream.core.runtime.tree.Node
import io.codestream.util.Eval
import io.codestream.util.transformation.TransformerService

class CompositeTask(
        val taskId: TaskId,
        val descriptor: TaskDescriptor,
        var errorTask: SimpleTaskHandler? = null,
        var finallyTask: SimpleTaskHandler? = null,
        scriptObjects: Map<String, Any> = emptyMap()) : Branch<StreamContext>(taskId.toString(), false), SimpleTask {

    private val nestedContext = StreamContext()

    init {
        scriptObjects.forEach { (k, v) ->
            nestedContext.bindings[k] = v
        }
    }

    override fun preTraversal(ctx: StreamContext) = BranchProcessingDirective.continueExecution

    override fun postTraversal(ctx: StreamContext) = BranchProcessingDirective.done

    override fun onError(error: TaskError, ctx: StreamContext) {
        ctx.bindings["_error_"] = error
        runTask(ctx, errorTask)
    }

    override fun enterBranch(ctx: StreamContext) {
    }

    override fun exitBranch(ctx: StreamContext) {
        runTask(ctx, finallyTask)
    }


    fun registerTask(module: CodestreamModule, value: Node<StreamContext> = this) {
        when (value) {
        //root task
            is CompositeTask -> {
                value.errorTask?.let {
                    nestedContext.registerTask(it.taskDef, module)
                }
                value.finallyTask?.let {
                    nestedContext.registerTask(it.taskDef, module)
                }
                value.children.forEach { registerTask(module, it) }
            }
            is GroupTaskHandler -> {
                nestedContext.registerTask(value.taskDef, module)
                value.children.forEach { registerTask(module, it) }
            }
            is SimpleTaskHandler -> {
                nestedContext.registerTask(value.taskDef, module)
            }
            else -> throw IllegalStateException("can handle ${value::class.simpleName}")
        }
    }

    override fun run(ctx: RunContext) {
        val context = ctx["_ctx"] as StreamContext?
                ?: throw ComponentFailedException(taskId.id, "context not loaded in Task Context")
        val defn = TaskDefContext.defn
        descriptor.parameters.forEach { k, v ->
            val paramDefn = defn.parameters[k]
            if (v.required && paramDefn == null) {
                throw ComponentDefinitionException(taskId.stringId, "Required parameter is missing")
            }
            val evaledResult = Eval.evalIfScript<Any?>(paramDefn?.valueDefn, context.bindings)?.let {
                TransformerService.convert<Any?>(it, v.type.typeMapping)
            }
            v.isValid(evaledResult)?.let {
                throw ComponentDefinitionException(taskId.toString(), it.toStringWithDescriptions())
            }
            nestedContext.bindings[k] = evaledResult
        }
        this.execute(nestedContext)
    }

    private fun runTask(ctx: StreamContext, targetTask: SimpleTaskHandler?) {
        targetTask?.execute(ctx)
    }

    override fun toString(): String {
        return "CompositeTask(taskId=$taskId)"
    }


}