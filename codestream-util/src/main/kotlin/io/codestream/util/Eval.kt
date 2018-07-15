package io.codestream.util

import java.io.Reader
import javax.script.*


object Eval {

    private val factory = ScriptEngineManager()
    private val defaultScriptEngine = "js"
    private val engines = mutableMapOf<String, ScriptEngine>()


    val defaultEngine get() = engineByName(defaultScriptEngine)


    fun engineByName(name: String): ScriptEngine {
        return engines.getOrPut(name) { factory.getEngineByName(name) }
    }


    fun <K> eval(script: String, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
        val ctx = createBindings(variables)
        @Suppress("UNCHECKED_CAST")
        return scriptEngine.eval(script, ctx) as K
    }

    fun <K> eval(script: Reader, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
        val ctx = createBindings(variables)
        @Suppress("UNCHECKED_CAST")
        return scriptEngine.eval(script, ctx) as K
    }

    private fun createBindings(variables: Map<String, Any?>): Bindings {
        val ctx = if (variables is Bindings) variables else {
            val newContext = SimpleScriptContext()
            val engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE)
            variables.forEach({ key: String, value: Any? -> engineScope.put(key, value) })
            engineScope
        }
        return ctx
    }

    fun <K> evalIfScript(candidate:Any?, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
        if (candidate == null) {
            return candidate as K
        }
        return if (isScriptString(candidate.toString())) {
            val script = extractScriptString(candidate.toString())
            eval(script, variables, scriptEngine) as K
        } else {
            candidate as K
        }
    }



    fun isScriptString(expr: String): Boolean {
        val isDollarForm = expr.stringify().startsWith("\${") && expr.stringify().endsWith("}")
        val isHashForm = expr.stringify().startsWith("#{") && expr.stringify().endsWith("}")
        return isDollarForm || isHashForm
    }

    fun extractScriptString(expr: String): String {
        if (isScriptString(expr)) {
            val stringify = expr.stringify()
            return stringify.substring(2, stringify.length - 1)
        }
        return expr
    }
}