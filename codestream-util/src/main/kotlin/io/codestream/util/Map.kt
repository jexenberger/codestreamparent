package io.codestream.util

val <T : Any, K : Any> Map<T, K>.pairs: Array<Pair<T, K>>
    get() = this.map { it.key to it.value }.toTypedArray()
