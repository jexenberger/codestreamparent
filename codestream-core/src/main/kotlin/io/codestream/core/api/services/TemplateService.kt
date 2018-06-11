package io.codestream.core.api.services


interface TemplateService {

    fun generate(template: String, values: Map<String, Any?>): String

    fun generate(template: String, vararg pair: Pair<String, Any>) = generate(template, pair.toMap())

}