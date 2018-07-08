@file:JvmName("Main")

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import io.codestream.core.api.Codestream

fun main(args: Array<String>) {
    val app = CliApp(ArgParser(arrayOf("run", "sys::echo", "-Ivalue=john")))
    app.run()


}