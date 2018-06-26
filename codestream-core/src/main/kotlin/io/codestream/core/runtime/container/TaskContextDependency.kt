package io.codestream.core.runtime.container

import io.codestream.core.api.annotations.TaskContext
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.di.api.*

class TaskContextDependency : AnnotationDependency<TaskContext>(TaskContext::class)  {
    override fun <T> resolve(annotation: TaskContext, target: DependencyTarget, ctx: Context): T {
        val streamCtx = ctx as StreamContext
        val id = TaskScopeId(streamCtx, target.id as TaskId)
        if (!ctx.hasComponent(id)) {
            addType<T>(target.targetType) withId(id) toScope TaskScope.id into (ctx as DefinableContext)
        }
        return ctx.get<T>(id)!!
    }
}