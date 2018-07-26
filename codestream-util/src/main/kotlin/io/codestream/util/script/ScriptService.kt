package io.codestream.util.script

import java.io.Reader
import java.io.File
import kotlin.reflect.KClass

interface ScriptService {


    fun loadClass(source:File, parentLoader:ClassLoader, language:String) : KClass<*>

    fun <T> compile(source: Reader, parentLoader: ClassLoader, language: String) = Eval.compile(source, Eval.engineByName(language))

    fun <T> eval(source: Reader, bindings: Map<String, Any?>, language: String): T {
        return Eval.eval(source, bindings, scriptEngine = Eval.engineByName(language))
    }

    fun <T> eval(source: String, bindings: Map<String, Any?>, checkForScript: Boolean, language: String, defaultValue: () -> T): T {
        val isScriptString = Eval.isScriptString(source)
        if (checkForScript && !isScriptString) return defaultValue()
        val script = if (isScriptString) Eval.extractScriptString(source) else source
        return Eval.eval(script, bindings, scriptEngine = Eval.engineByName(language))
    }
}