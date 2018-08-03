/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:JvmName("Main")

package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
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
    Console.display(bold("Running...")).newLine()
    val app = CliApp(ArgParser(args))
    try {
        app.run()
    } catch (e:ShowHelpException) {
        e.toString()
    }


}