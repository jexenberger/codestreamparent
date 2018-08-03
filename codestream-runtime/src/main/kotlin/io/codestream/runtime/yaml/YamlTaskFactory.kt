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

package io.codestream.runtime.yaml

import io.codestream.api.Task
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import io.codestream.api.TaskId
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory

class YamlTaskFactory(val module: BaseYamlModule) : Factory<Task> {
    override fun get(id: ComponentId, ctx: Context): CompositeTask {
        val streamContext = ctx as StreamContext
        val compositeTask = module.getCompositeTask(id as TaskId, streamContext)
        streamContext.registerTask(id, compositeTask)
        compositeTask.registerTask(module)
        return compositeTask
    }

}