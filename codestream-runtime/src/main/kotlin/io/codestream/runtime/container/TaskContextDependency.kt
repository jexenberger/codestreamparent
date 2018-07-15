package io.codestream.runtime.container

import io.codestream.api.annotations.TaskContext
import io.codestream.runtime.StreamContext
import io.codestream.api.TaskId
import io.codestream.di.api.*

class TaskContextDependency : AnnotationDependency<TaskContext>(TaskContext::class)  {
    override fun <T> resolve(annotation: TaskContext, target: DependencyTarget, ctx: Context): T? {
        val streamCtx = ctx as StreamContext
        val id = io.codestream.runtime.container.TaskScopeId(streamCtx, target.id as TaskId)
        if (!ctx.hasComponent(id)) {
            addType<T>(target.targetType) withId(id) toScope io.codestream.runtime.container.TaskScope.id into (ctx as DefinableContext)
        }
        return ctx.get<T>(id)!!
    }
}