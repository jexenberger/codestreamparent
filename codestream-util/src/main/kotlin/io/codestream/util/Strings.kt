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

package io.codestream.util

import java.io.File
import java.util.concurrent.TimeUnit

fun Any?.stringify(): String = this.toString().trim()


fun String.exec(dir: File = File(System.getProperty("user.dir")),
                timeout: Long = 60,
                timeUnit: TimeUnit = TimeUnit.MINUTES): Pair<Int, String> {
    val parts = this.split(" ")
    val process = ProcessBuilder(parts)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .directory(dir)
            .start()
    val output = process.inputStream.bufferedReader().readText()
    val completed = process.waitFor(timeout, timeUnit)
    if (!completed) {
        return Pair(-1, "TIMEOUT")
    }
    val result = process.exitValue()
    return Pair(result, output)
}

fun String.exec(dir: File = File(System.getProperty("user.dir")),
                timeout: Long = 60,
                timeUnit: TimeUnit = TimeUnit.MINUTES,
                handler: (String) -> Unit): Int {
    val parts = this.split(" ").map { it.trim() }
    return system.exec(parts.toTypedArray(),
            dir = dir,
            timeout = timeout,
            timeUnit = timeUnit,
            handler = handler)
}


object Strings {
    fun isEmpty(str: String?): Boolean {
        return (str == null || str.trim().equals(""))
    }
}