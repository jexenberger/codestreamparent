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

package io.codestream.cli

import io.codestream.api.Codestream
import io.codestream.api.ModuleId
import io.codestream.api.TaskType
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Value
import io.codestream.doc.ModuleDoc
import io.codestream.doc.TaskDoc
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.bold
import io.codestream.util.io.console.decorate

class HelpCommandlet(
        @Inject
        val codestream: Codestream,
        @Value("task.ref")
        val task: String
) : Commandlet {

    override fun run() {

        when (task) {
            "" -> {
                headerAndDescription("Help Options", "List of available functions to get help on (cs help [option])")
                displayListLine("modules","Displays the list of available modules")
                displayListLine("[module name]","Displays details about a specific module")
                displayListLine("[task]","Displays details about a specific task")
            }
            "modules" -> {
                codestream.modules.forEach { id ->

                    codestream.moduleDoc(id)?.let {
                        Console.display(bold(id.toString()).padEnd(30))
                                .display(it.description)
                                .newLine()

                    } ?: throw IllegalStateException("Module '$id' has no documentation???")
                }
            }
            else -> {
                if (task.contains("::")) {
                    val type = TaskType.fromString(task)
                    codestream.moduleDoc(type.module)?.let {
                        it.taskDoc(type.name)?.let {
                            displayTask(type, it)
                        }
                    }
                } else {
                    val module = ModuleId.fromString(task)
                    if (module.isDefaultVersion) {
                        codestream.modulesByName(module.name).forEach {
                            displayModule(it, codestream.moduleDoc(it)!!)
                        }
                    } else {
                        codestream.moduleDoc(module)?.let { displayModule(module, it) }
                    }
                }
            }

        }
        Console.newLine().newLine()

    }


    fun displayTask(type:TaskType, doc:TaskDoc) {
        headerAndDescription(type.taskName, doc.description)
        Console.display(heading("Parameters")).newLine().newLine()
        doc.parameters.forEach {
            displayListLine(it.name, it.description)
            displaySubListLine("Required:  ${it.required}")
            displaySubListLine("Type:      ${it.type}")
            it.pattern?.let {
                if (it.isNotEmpty()) displaySubListLine("Regex:      ${it}")
            }
            it.pattern?.let {
                if (it.isNotEmpty()) displaySubListLine("Default:    ${it}")
            }
            Console.newLine()
        }
        doc.returnType?.let {
            Console.display(heading("Return type")).newLine().newLine()
            displayListLine("Type", it.first)
            displayListLine("Description", it.second)
        }
    }

    fun displayModule(id: ModuleId, doc: ModuleDoc) {
        headerAndDescription(id.toString(), doc.description)
        Console.display(heading("Tasks")).newLine().newLine()
        doc.tasks.forEach { task ->
            displayListLine(task.name, task.description)
        }
        Console.newLine()
        Console.display(heading("Functions")).newLine().newLine()
        doc.functions.forEach { func ->
            displayListLine(func.name, func.description)
            func.params.forEach {(name, description) ->
                displaySubListLine("${name.padEnd(10)}: $description")
            }
        }
    }

    private fun heading(heading: String) = decorate(heading, Console.BOLD, Console.UNDERLINE)

    private fun displayListLine(value: String, description: String) {
        Console.display(bold(value).padEnd(30))
                .display(":")
                .space()
                .display(description)
                .newLine()
    }
    private fun displaySubListLine(description: String) {
        Console.display("".padEnd(30))
                .display("-")
                .space()
                .display(description)
                .newLine()
    }

    private fun headerAndDescription(id: String, desc: String) {
        Console.display(heading(id))
                .newLine()
                .newLine()
                .display(bold(desc))
                .newLine()
                .newLine()
    }
}