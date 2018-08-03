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

package io.codestream.util.io.console

import io.codestream.util.OS
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicLong


fun decorate(value: Any?, vararg codes: String): String {
    return "${codes.joinToString("")}$value${Console.ANSI_RESET}"
}

fun info(value: Any?) = decorate(value, Console.ANSI_GREEN)
fun warn(value: Any?) = decorate(value, Console.ANSI_RED)
fun bold(value: Any?) = decorate(value, Console.BOLD)

object Console {

    val ANSI_RESET = "\u001b[0m"
    val ANSI_RED = "\u001B[31m"
    val ANSI_GREEN = "\u001B[32m"
    val OVERWRITE = "\u001b[1000D"
    val BOLD = "\u001b[1m"
    val UNDERLINE = "\u001b[4m"
    val REVERSED = "\u001b[7m"
    val UP = "\u001b[1A"

    private val lineCount: AtomicLong = AtomicLong();


    val out: PrintWriter by lazy {
        if (System.console() != null) System.console().writer() else PrintWriter(OutputStreamWriter(System.out))
    }

    val stdin: BufferedReader by lazy {
        if (System.console() != null) BufferedReader(System.console().reader()) else BufferedReader(InputStreamReader(System.`in`))
    }

    fun getSecret() = System.console()?.let { String(it.readPassword()) } ?: readLine()

    fun get(): String = stdin.readLine()!!

    fun getNullForBlank(): String? {
        val res = stdin.readLine()!!
        if (res.trim().equals("")) {
            return null
        }
        return res
    }

    fun getChar(): Char = stdin.read().toChar()

    fun display(value: Any?): Console {
        value?.let { out.print(it) }
        out.flush()
        return this
    }

    fun space(): Console {
        return display(" ")
    }

    fun tab(): Console {
        return display("\t")
    }

    fun newLine(): Console {
        lineCount.incrementAndGet()
        return display(OS.os().newLine)
    }

}