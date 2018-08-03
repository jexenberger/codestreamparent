/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.util.script

import io.codestream.util.stringify
import org.mvel2.jsr223.MvelScriptEngineFactory
import java.io.Reader
import javax.script.*


object Eval {

    private val factory = ScriptEngineManager()
    private val defaultScriptEngine = "mvel"
    private val engines = mutableMapOf<String, ScriptEngine>()

    init {
        //thanks java 9
        engines["mvel"] = MvelScriptEngineFactory().scriptEngine!!
    }


    val defaultEngine get() = engineByName(defaultScriptEngine)


    fun engineByName(name: String): ScriptEngine {
        return engines.getOrPut(name) { factory.getEngineByName(name) }
    }

    fun engineByExtension(name: String): ScriptEngine {
        return engines.getOrPut(name) { factory.getEngineByExtension(name) }
    }

    fun compile(script:Reader, scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)) = (scriptEngine as Compilable).compile(script)

    fun compile(script:String, scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)) = (scriptEngine as Compilable).compile(script)


    fun <T> compiledScript(script:String, scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): Jsr223Compilable<T> {
        return Jsr223Compilable(compile(script, scriptEngine))
    }

    fun <T> compiledScript(script:Reader, scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): Jsr223Compilable<T> {
        return Jsr223Compilable(compile(script, scriptEngine))
    }


    fun <K> eval(script: String, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
        val ctx = createBindings(variables)
        @Suppress("UNCHECKED_CAST")
        return scriptEngine.eval(script, ctx) as K
    }

    fun <K> eval(script: CompiledScript, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
        val ctx = createBindings(variables)
        @Suppress("UNCHECKED_CAST")
        return script.eval(ctx) as K
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
            variables.forEach { key: String, value: Any? -> engineScope.put(key, value) }
            engineScope
        }
        return ctx
    }

    fun <K> evalIfScript(candidate: Any?, variables: Map<String, Any?> = mapOf(), scriptEngine: ScriptEngine = engineByName(defaultScriptEngine)): K {
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