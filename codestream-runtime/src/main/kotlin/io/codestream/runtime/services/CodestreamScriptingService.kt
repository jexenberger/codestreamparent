package io.codestream.runtime.services

import groovy.lang.GroovyClassLoader
import io.codestream.api.services.Language
import io.codestream.api.services.ScriptService
import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.reflect.KClass

class CodestreamScriptingService : ScriptService {

    override fun loadClass(source: File, parentLoader: ClassLoader, language:Language): KClass<*> {
        if (!language.equals(Language.groovy)) {
            throw UnsupportedOperationException("$language is not current supported, only ${Language.groovy} is supported")
        }
        return GroovyClassLoader(parentLoader).parseClass(source).kotlin
    }


}