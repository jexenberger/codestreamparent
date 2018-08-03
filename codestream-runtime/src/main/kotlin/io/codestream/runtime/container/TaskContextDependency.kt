/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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