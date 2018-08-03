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

package io.codestream.runtime.services

import io.codestream.api.services.TemplateService
import io.codestream.di.api.Context
import org.mvel2.templates.TemplateCompiler
import org.mvel2.templates.TemplateRuntime
import java.io.*

class MvelTemplateService : TemplateService {

    override fun write(source: InputStream, target: OutputStream, ctx: Context) {
        return write(source, target, ctx.bindings)
    }

    override fun write(source: InputStream, target: OutputStream, variables: Map<String, Any?>) {
        val template = TemplateCompiler.compileTemplate(source)
        val result =  TemplateRuntime.execute(template, variables)
        target.write(result.toString().toByteArray())
    }

}