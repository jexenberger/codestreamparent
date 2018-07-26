package io.codestream.util.script

interface Evaluatable<T> {

    fun eval(ctx: MutableMap<String, Any?>) : T



}