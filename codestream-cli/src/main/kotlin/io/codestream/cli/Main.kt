@file:JvmName("Main")

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import io.codestream.util.script.Eval
import io.codestream.util.system
import sun.misc.Unsafe

fun disableIllegalAccessWarnings() {
    try {
        val theUnsafe = Unsafe::class.java!!.getDeclaredField("theUnsafe")
        theUnsafe.setAccessible(true)
        val u = theUnsafe.get(null) as Unsafe

        val cls = Class.forName("jdk.internal.module.IllegalAccessLogger")
        val logger = cls.getDeclaredField("logger")
        u.putObjectVolatile(cls, u.staticFieldOffset(logger), null)
    } catch (e: Exception) {
        // ignore
    }

}

fun main(args: Array<String>) {
    val app = CliApp(ArgParser(args))
    try {
        app.run()
    } catch (e:ShowHelpException) {
        e.toString()
    }


}