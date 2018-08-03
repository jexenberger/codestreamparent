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

import io.codestream.util.csv.CSVLine
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class CSVLineTest {

    @Test
    fun test_no_quote() {

        val line = "10,AU,Australia"
        val result = CSVLine.parseLine(line)

        assertEquals(result.size, 3)
        assertEquals(result.get(0), "10")
        assertEquals(result.get(1), "AU")
        assertEquals(result.get(2), "Australia")

    }

    @Test
    @Throws(Exception::class)
    fun test_no_quote_but_double_quotes_in_column() {

        val line = "10,AU,Aus\"\"tralia"

        val result = CSVLine.parseLine(line)
        assertEquals(result.size, 3)
        assertEquals(result.get(0), "10")
        assertEquals(result.get(1), "AU")
        assertEquals(result.get(2), "Aus\"tralia")

    }

    @Test
    fun test_double_quotes() {

        val line = "\"10\",\"AU\",\"Australia\""
        val result = CSVLine.parseLine(line)

        assertEquals(result.size, 3)
        assertEquals(result.get(0), "10")
        assertEquals(result.get(1), "AU")
        assertEquals(result.get(2), "Australia")

    }

    @Test
    fun test_double_quotes_but_double_quotes_in_column() {

        val line = "\"10\",\"AU\",\"Aus\"\"tralia\""
        val result = CSVLine.parseLine(line)

        assertEquals(result.size, 3)
        assertEquals(result.get(0), "10")
        assertEquals(result.get(1), "AU")
        assertEquals(result.get(2), "Aus\"tralia")

    }

    @Test
    fun test_double_quotes_but_comma_in_column() {

        val line = "\"10\",\"AU\",\"Aus,tralia\""
        val result = CSVLine.parseLine(line)

        assertEquals(result.size, 3)
        assertEquals(result.get(0), "10")
        assertEquals(result.get(1), "AU")
        assertEquals(result.get(2), "Aus,tralia")

    }

    @Test
    internal fun testToLine() {
        val line = CSVLine("\"hello\", \"world\"")
        println(line.line)
        assertEquals("hello,world", line.toLine())
        assertEquals("'hello','world'", line.toLine(quoteChar = '\''))
    }

    @Test
    internal fun testToMap() {
        val line = CSVLine("\"hello\", \"world\"")
        val map = line.mapTo(arrayOf("A", "B"))
        assertEquals("hello", map["A"])
        assertEquals("world", map["B"])
    }
}