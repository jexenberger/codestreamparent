package io.codestream.core.runtime.services

import com.samskivert.mustache.Mustache
import io.codestream.core.api.services.TemplateService
import io.codestream.di.api.Context
import java.io.Reader
import java.io.Writer


class JMustacheTemplatingService : TemplateService {


     override fun write(source: Reader, target: Writer, ctx: Context) {
        val template = Mustache.compiler().compile(source)
        template.execute(ctx.bindings, target)
    }
}