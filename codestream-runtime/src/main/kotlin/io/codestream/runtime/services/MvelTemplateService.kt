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