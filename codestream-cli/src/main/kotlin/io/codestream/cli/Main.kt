@file:JvmName("Main")

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import io.codestream.api.Codestream

fun main(args: Array<String>) {
    val app = CliApp(ArgParser(args))
    try {
        app.run()
    } catch (e:ShowHelpException) {
        e.toString()
    }


}