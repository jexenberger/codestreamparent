package io.codestream.core.api

interface RunContext {

    fun put(key: String, value: Any?): Any?
    operator fun get(key: String): Any?
    operator fun set(key: String, value: Any) = put(key, value)
}