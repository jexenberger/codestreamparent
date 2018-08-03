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