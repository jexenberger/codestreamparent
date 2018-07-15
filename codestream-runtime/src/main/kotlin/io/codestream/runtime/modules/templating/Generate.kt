package io.codestream.runtime.modules.templating

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.services.TemplateService
import io.codestream.di.annotation.Inject
import java.io.File
import java.io.StringReader
import java.io.StringWriter

@Task(name = "generate", description = "Generates output from a Mustache template from a file or string template")
class Generate(
        @Parameter(description = "File or template content")
        val template: String,
        @Parameter(description = "Parameters to generate the template with")
        val parameters: Map<String, Any?>,
        @Inject
        val templateService: TemplateService,
        @Parameter(description = "Name of output variable to store template result")
        override val outputVariable: String = "__output"
) : FunctionalTask {

    override fun getResult(ctx: RunContext): Any? {
        val file = File(template)
        val buffer = StringWriter()
        val reader = if (file.isFile) {
            file.reader()
        } else {
            StringReader(template)
        }
        templateService.write(reader, buffer, ctx as Map<String, Any>)
        return buffer.toString()
    }

}