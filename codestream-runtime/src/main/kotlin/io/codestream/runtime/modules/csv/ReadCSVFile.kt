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

package io.codestream.runtime.modules.csv

import io.codestream.api.ComponentFailedException
import io.codestream.api.Directive
import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.TaskContext
import io.codestream.util.csv.CSVLine
import java.io.BufferedReader

import java.io.File

class ReadCSVFile(
        @Parameter(description = "path to CSV file to parse")
        val file: File,
        @Parameter(description = "name of variable to write each line as as keyValue key with the headers, if no headers are specified the position is used")
        val outputVariable: String = "__csv",
        @Parameter(description = "use the first line of the file to to define headers")
        val useFirstLineAsHeader: Boolean = false,
        @Parameter(description = "headers in order of columns in the file")
        val headers: Array<String> = emptyArray(),
        @TaskContext
        val context: ReadCSVFileContext
) : GroupTask {
    override fun before(ctx: RunContext): Directive {
        loadFile()
        if (context.firstTime) {
            context.csvFile = loadFile()
            val line = context.csvFile?.readLine()
            if (line == null) {
                return Directive.done
            }
            if (useFirstLineAsHeader) {
                val headers = CSVLine.parseLine(line)
                val nextLine = context.csvFile?.readLine()
                if (nextLine == null) {
                    return Directive.done
                }
                val csvRecord = CSVLine(nextLine).mapTo(headers.toTypedArray())
                ctx[outputVariable] = csvRecord
                return Directive.continueExecution
            }
            val csv = CSVLine(line)
            val headers = if (this.headers.isEmpty()) (0..csv.line.size).map { it.toString() }.toTypedArray() else headers
            ctx[outputVariable] = csv.mapTo(headers)
        }
        return Directive.abort
    }

    private fun loadFile(): BufferedReader {
        if (!file.isFile) {
            throw ComponentFailedException("id", "${file.absolutePath} does not exist or is a directory")
        }
        return BufferedReader(file.reader())
    }

    override fun after(ctx: RunContext) : Directive {
        this.context.csvFile?.close()
        return Directive.done
    }

    override fun onFinally(ctx: RunContext) {

    }
}