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

package io.codestream.runtime.modules.http

import io.codestream.api.annotations.Task
import io.codestream.util.rest.Request
import io.codestream.util.rest.Response
import java.nio.file.Files

@Task(name = "post", description = "Performs an HTTP Post")
class Post : BaseBodiedHttpTask() {
    override fun handleRequest(request: Request): Response {
        if (body == null && attachments == null) {
            throw IllegalStateException("Both body and attachments are empty")
        }
        request.body(body!!)
        attachments?.forEach {
            request.attachment(it, Files.probeContentType(it.toPath()))
        }
        return request.post()
    }

}