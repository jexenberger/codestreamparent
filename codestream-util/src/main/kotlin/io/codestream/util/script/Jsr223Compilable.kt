package io.codestream.util.script

import javax.script.CompiledScript

class Jsr223Compilable<T>(val script:CompiledScript) : Evaluatable<T> {

    override fun eval(ctx: MutableMap<String, Any?>) = Eval.eval(script, ctx) as T
}