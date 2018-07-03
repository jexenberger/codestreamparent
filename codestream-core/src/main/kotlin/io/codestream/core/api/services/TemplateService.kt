package io.codestream.core.api.services

import io.codestream.di.api.Context
import java.io.Reader
import java.io.Writer


interface TemplateService {

    fun write(source: Reader, target: Writer, ctx: Context)

}