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

package io.codestream.runtime.modules.templating

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.services.TemplateService
import io.codestream.di.annotation.Inject
import java.io.*

@Task(name = "generate", description = "Generates output from a Mustache template from a file or string template")
class Generate(
        @Parameter(description = "File or template content")
        val template: String,
        @Parameter(description = "Parameters to generate the template with")
        val parameters: Map<String, Any?>,
        @Inject
        val templateService: TemplateService
) : FunctionalTask<String> {

    override fun evaluate(ctx: RunContext): String? {
        val file = File(template)
        val buffer = ByteArrayOutputStream()
        val reader = if (file.isFile) {
            file.inputStream()
        } else {
            ByteArrayInputStream(template.toByteArray())
        }
        templateService.write(reader, buffer, parameters)
        return String(buffer.toByteArray())
    }

}