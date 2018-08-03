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

package io.codestream.util.csv


class CSVLine(val line: List<String>, val separatorChar: Char = defaultSeperator, val quoteChar: Char? = null) : AbstractList<String>() {

    constructor(line:String, separatorChar: Char = defaultSeperator, quoteChar: Char = defaultQuote)
            : this(parseLine(line, separatorChar, quoteChar))


    override val size: Int get() = line.size

    override fun get(index: Int) = line[index]


    fun toLine(separatorChar: Char = this.separatorChar, quoteChar: Char? = this.quoteChar) : String{
        val quoteToUse = quoteChar?.toString() ?: defaultQuote.toString()
        return line.map { if (quoteChar != null) "$quoteToUse$it$quoteToUse" else it}.joinToString(separatorChar.toString())
    }

    fun mapTo(fields:Array<String>) : Map<String, String?> {
        if (fields.size < line.size) {
            throw IllegalArgumentException("array of fields cannot be less than the columns of the CSV record")
        }
        val map = mutableMapOf<String, String?>()
        val line = this.line.iterator()
        for (field in fields) {
           val value = if (line.hasNext()) line.next() else null
           map[field] = value
        }
        return map
    }

    override fun toString() = toLine()

    companion object {

        val defaultQuote = '"'
        val defaultSeperator = ','

        fun toCSVLine(cvsLine: String, separatorChar: Char = defaultSeperator, quoteChar: Char = defaultQuote): CSVLine {
            return CSVLine(parseLine(cvsLine, separatorChar, quoteChar))
        }

        fun parseLine(cvsLine: String, separatorChar: Char = defaultSeperator, quoteChar: Char = defaultQuote): List<String> {
            val result = mutableListOf<String>()
            if (cvsLine.isEmpty()) {
                return result
            }
            val customQuote = if (quoteChar == ' ') defaultQuote else quoteChar
            val separators = if (separatorChar == ' ') defaultSeperator else separatorChar

            var curVal = StringBuffer()
            var inQuotes = false
            var startCollectChar = false
            var doubleQuotesInColumn = false

            val chars = cvsLine.toCharArray()

            for (ch in chars) {

                if (inQuotes) {
                    startCollectChar = true
                    if (ch == customQuote) {
                        inQuotes = false
                        doubleQuotesInColumn = false
                    } else {

                        //Fixed : allow "" in custom quote enclosed
                        if (ch == '\"') {
                            if (!doubleQuotesInColumn) {
                                curVal.append(ch)
                                doubleQuotesInColumn = true
                            }
                        } else {
                            curVal.append(ch)
                        }

                    }
                } else {
                    var continueStuff = false
                    when (ch) {
                        customQuote -> {
                            inQuotes = true

                            //Fixed : allow "" in empty quote enclosed
                            if (chars[0] != '"' && customQuote == '\"') {
                                curVal.append('"')
                            }

                            //double quotes in column will hit this!
                            if (startCollectChar) {
                                curVal.append('"')
                            }
                        }
                        separators -> {
                            result.add(curVal.toString())

                            curVal = StringBuffer()
                            startCollectChar = false
                        }
                        '\r' -> continueStuff = true
                        '\n' -> continueStuff = true
                        ' ' -> continueStuff = !startCollectChar
                        else -> curVal.append(ch)
                    }
                    if (continueStuff) {
                        continue
                    }
                }

            }

            result.add(curVal.toString())

            return result
        }

    }


}