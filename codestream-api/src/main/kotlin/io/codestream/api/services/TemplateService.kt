package io.codestream.api.services

import io.codestream.di.api.Context
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer


interface TemplateService {

    fun write(source: InputStream, target: OutputStream, ctx: Context)
    fun write(source: InputStream, target: OutputStream, variables: Map<String, Any?>)

}