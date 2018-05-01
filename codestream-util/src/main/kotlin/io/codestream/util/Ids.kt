package io.codestream.util

import java.util.concurrent.atomic.AtomicLong

object Ids {

    private val ids:MutableMap<String, AtomicLong>  = mutableMapOf()

    fun next(ctx:String = "default") : Long {
        if (!ids.containsKey(ctx)) {
            synchronized(ids) {
                ids[ctx] = AtomicLong(0)
            }
        }
        return ids[ctx]!!.incrementAndGet()
    }
}