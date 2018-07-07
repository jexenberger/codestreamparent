@file:JvmName("Main")
package io.codestream.cli

import io.codestream.core.api.Codestream
import io.codestream.core.api.ModuleId

fun main(args : Array<String>) {
    val codestream = Codestream.get()
    println(codestream.modules)
    codestream.runTask(
            module = ModuleId("sample"),
            task = "greeter",
            parameters = mapOf("name" to "John"))

}