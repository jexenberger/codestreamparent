package io.codestream.runtime.services

import com.samskivert.mustache.Mustache
import io.codestream.api.services.TemplateService
import io.codestream.di.api.Context
import java.io.Reader
import java.io.Writer


class JMustacheTemplatingService : TemplateService {
    override fun write(source: Reader, target: Writer, variables: Map<String, Any?>) {
        val template = Mustache.compiler().compile(source)
        template.execute(variables, target)
    }


    override fun write(source: Reader, target: Writer, ctx: Context) {
        val template = Mustache.compiler().compile(source)
        template.execute(ctx.bindings, target)
    }
}