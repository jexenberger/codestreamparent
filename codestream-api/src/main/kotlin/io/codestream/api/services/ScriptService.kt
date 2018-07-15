package io.codestream.api.services

import io.codestream.util.Eval
import java.io.Reader
import java.io.File
import kotlin.reflect.KClass

interface ScriptService {


    fun loadClass(source:File, parentLoader:ClassLoader, language:Language) : KClass<*>

    fun <T> eval(source: Reader, bindings: Map<String, Any?>, language: Language): T {
        return Eval.eval(source, bindings, scriptEngine = Eval.engineByName(language.engineName))
    }

    fun <T> eval(source: String, bindings: Map<String, Any?>, checkForScript: Boolean, language: Language, defaultValue: () -> T): T {
        val isScriptString = Eval.isScriptString(source)
        if (checkForScript && !isScriptString) return defaultValue()
        val script = if (isScriptString) Eval.extractScriptString(source) else source
        return Eval.eval(script, bindings, scriptEngine = Eval.engineByName(language.engineName))
    }
}