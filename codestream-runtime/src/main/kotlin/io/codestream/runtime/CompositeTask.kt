package io.codestream.runtime

import io.codestream.api.*
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.runtime.task.GroupTaskHandler
import io.codestream.runtime.task.SimpleTaskHandler
import io.codestream.runtime.task.TaskDefContext
import io.codestream.runtime.tree.Branch
import io.codestream.api.Directive
import io.codestream.runtime.tree.Node
import io.codestream.util.Eval
import io.codestream.util.transformation.TransformerService

class CompositeTask(
        val taskId: TaskId,
        val descriptor: TaskDescriptor,
        val nestedContext: io.codestream.runtime.StreamContext,
        var errorTask: SimpleTaskHandler? = null,
        var finallyTask: SimpleTaskHandler? = null) : Branch<io.codestream.runtime.StreamContext>(taskId.toString(), false), SimpleTask {



    override fun preTraversal(ctx: io.codestream.runtime.StreamContext) = Directive.continueExecution

    override fun postTraversal(ctx: io.codestream.runtime.StreamContext) = Directive.done

    override fun onError(error: Exception, ctx: io.codestream.runtime.StreamContext) {
        val taskError = TaskError(error, ctx.bindings)
        ctx.bindings["_error_"] = taskError
        errorTask?.let {  runTask(ctx, it) } ?: throw taskError
    }

    override fun enterBranch(ctx: io.codestream.runtime.StreamContext) {
    }

    override fun exitBranch(ctx: io.codestream.runtime.StreamContext) {
        runTask(ctx, finallyTask)
    }


    fun registerTask(module: CodestreamModule, value: Node<io.codestream.runtime.StreamContext> = this) {
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
        val context = ctx["_ctx"] as io.codestream.runtime.StreamContext?
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

    private fun runTask(ctx: io.codestream.runtime.StreamContext, targetTask: SimpleTaskHandler?) {
        targetTask?.execute(ctx)
    }

    override fun toString(): String {
        return "CompositeTask(taskId=$taskId)"
    }


}