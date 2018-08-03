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

package io.codestream.api

import io.codestream.api.TaskType
import io.codestream.di.api.ComponentId
import java.util.*

data class TaskId(val taskType: TaskType, val id: String = UUID.randomUUID().toString()) : ComponentId {
    override val stringId: String
        get() = "${taskType.taskName}(${id})"


    override fun toString(): String {
        return stringId
    }


}