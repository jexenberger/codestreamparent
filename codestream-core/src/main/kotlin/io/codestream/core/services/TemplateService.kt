package io.codestream.core.services

import io.codestream.core.runtime.Context

interface TemplateService {

    fun generate(template:String, ctx: Context) : String

}