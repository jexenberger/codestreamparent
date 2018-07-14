@file:JvmName("Main")

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import io.codestream.api.Codestream

fun main(args: Array<String>) {
    val app = CliApp(ArgParser(args))
    app.run()


}