package io.codestream.util

class Timer(val start:Long = System.currentTimeMillis()) {

    val currentTimeTaken:Long get() = System.currentTimeMillis() - start


    companion object {
        fun <T> run(handler: () -> T) : Pair<Long, T> {
            val t = Timer()
            val res = handler()
            return t.currentTimeTaken to res
        }
    }


}