package io.codestream.runtime.services

import io.codestream.util.script.ScriptService
import java.io.File
import kotlin.reflect.KClass

class CodestreamScriptingService : ScriptService {

    override fun loadClass(source: File, parentLoader: ClassLoader, language: String): KClass<*> {
        return String::class
    }


}