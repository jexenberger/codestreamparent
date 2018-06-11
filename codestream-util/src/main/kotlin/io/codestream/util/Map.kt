package io.codestream.util

val <T , K > Map<T, K>.pairs: Array<Pair<T, K>>
    get() = this.map { it.key to it.value }.toTypedArray()


