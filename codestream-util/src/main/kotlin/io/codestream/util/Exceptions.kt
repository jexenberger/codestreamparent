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

