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

package io.codestream.api.events

import java.time.LocalDateTime
import java.util.*

open class CodestreamEvent(
        val desc:String,
        val id: String = UUID.randomUUID().toString(),
        val timeStamp: LocalDateTime = LocalDateTime.now()
) {
    override fun toString(): String {
        return "id=$id::timeStamp=$timeStamp[$desc]"
    }
}