package io.codestream.runtime

import io.codestream.api.*
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.api.metamodel.TaskDef
import io.codestream.api.resources.ResourceRepository
import io.codestream.util.script.ScriptService
import io.codestream.api.services.TemplateService
import io.codestream.di.api.TypeId
import io.codestream.di.api.addInstance
import io.codestream.runtime.task.GroupTaskHandler
import io.codestream.runtime.task.NonGroupTaskHandler
import io.codestream.runtime.task.TaskDefContext
import io.codestream.runtime.tree.Branch
import io.codestream.runtime.tree.Node
import io.codestream.util.script.Eval
import io.codestream.util.crypto.SimpleSecretStore
import io.codestream.util.transformation.TransformerService

open class CompositeTask(
        val taskId: TaskId,
        val descriptor: TaskDescriptor,
        val nestedContext: io.codestream.runtime.StreamContext,
        var errorTask: NonGroupTaskHandler? = null,
        var finallyTask: NonGroupTaskHandler? = null,
        var returnVal: Pair<Type, String>? = null
) : Branch<io.codestream.runtime.StreamContext>(taskId.toString(), false), FunctionalTask<Any> {


    override fun evaluate(ctx: RunContext): Any? {
        val taskDef = TaskDefContext.defn
        this.executeCompositeTask(ctx, taskDef)
        if (returnVal != null) {
            return nestedContext.bindings.get(returnVal!!.second)
        }
        return Unit
    }


    override fun preTraversal(ctx: io.codestream.runtime.StreamContext) = Directive.continueExecution

    override fun postTraversal(ctx: io.codestream.runtime.StreamContext) = Directive.done

    override fun onError(error: Exception, ctx: io.codestream.runtime.StreamContext) {
        val taskError = if (error is TaskError) error else TaskError(taskId, error, ctx.bindings)
        ctx.bindings["_error_"] = taskError
        errorTask?.let { runTask(ctx, it) } ?: throw taskError
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
            is NonGroupTaskHandler -> {
                nestedContext.registerTask(value.taskDef, module)
            }
            else -> throw IllegalStateException("can handle ${value::class.simpleName}")
        }
    }


    protected fun executeCompositeTask(ctx: RunContext, taskDef: TaskDef) {
        val context = ctx["_ctx"] as StreamContext?
                ?: throw ComponentFailedException(taskId.id, "context not loaded in Task Context")
        val defn = taskDef
        populateNewContext(context, nestedContext)
        descriptor.parameters.forEach { k, v ->
            val paramDefn = defn.parameters[k]
            if (v.required && paramDefn == null) {
                throw ComponentDefinitionException(taskId.stringId, "Required parameter '$k' is missing")
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

    private fun populateNewContext(oldCtx: StreamContext, newCtx: StreamContext) {
        oldCtx.get<SimpleSecretStore>(SimpleSecretStore::class)?.let { addInstance(it) withId TypeId(SimpleSecretStore::class) into newCtx }
        oldCtx.get<ScriptService>(ScriptService::class)?.let { addInstance(it) withId TypeId(ScriptService::class) into newCtx }
        oldCtx.get<ResourceRepository>(ResourceRepository::class)?.let { addInstance(it) withId TypeId(ResourceRepository::class) into newCtx }
        oldCtx.get<TemplateService>(TemplateService::class)?.let { addInstance(it) withId TypeId(TemplateService::class) into newCtx }
    }

    private fun runTask(ctx: io.codestream.runtime.StreamContext, targetTask: NonGroupTaskHandler?) {
        targetTask?.execute(ctx)
    }

    override fun toString(): String {
        return "CompositeTask(taskId=$taskId)"
    }


}