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

import java.io.StringWriter

val Throwable.rootCause: Throwable get() = getRootException(this)

val Throwable.stackDump: String get() = toStackTrace(this)

fun getRootException(ex: Throwable): Throwable {
    val cause = ex.cause
    return if (cause != null) {
        return getRootException(cause)
    } else {
        ex
    }
}

fun toStackTrace(ex: Throwable) : String {
    val writer = StringWriter()

    ex.stackTrace.forEach {
        writer.appendln(it.toString())
    }
    return writer.toString()
}

