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

package io.codestream.util.transformation

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals


class TransformerServiceTest {


    val test: Array<String> = arrayOf()


    @Test
    @Throws(Exception::class)
    fun testTransform() {


        assertEquals(true, TransformerService.convert(true, Boolean::class))
        assertEquals("1", TransformerService.convert(1))
        assertEquals(1.0, TransformerService.convert(1))
        assertEquals(1.0, TransformerService.convert(1))
        assertEquals(1.toShort(), TransformerService.convert(1))
        assertEquals(1.toByte(), TransformerService.convert(1))
        assertEquals("1", TransformerService.convert(1))
        assertEquals("1.0", TransformerService.convert(1.0))
        assertEquals("1.0", TransformerService.convert(1.0f))
        assertEquals("1", TransformerService.convert(1))
        assertEquals(true, TransformerService.convert(1))
        assertEquals(false, TransformerService.convert(0))
        assertEquals(true, TransformerService.convert('1'))
        assertEquals(false, TransformerService.convert('0'))
        assertEquals(true, TransformerService.convert('y'))
        assertEquals(false, TransformerService.convert('n'))
        assertEquals(true, TransformerService.convert("yes"))
        assertEquals(false, TransformerService.convert("no"))
        assertArrayEquals(arrayOf("1", "2"), TransformerService.convert("1,2", Array<String>::class))
        val testS: Array<String> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf("1", "2", "3"), testS)
        val testI: Array<Int> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf(1, 2, 3), testI)
        val testL: Array<Long> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf(1L, 2L, 3L), testL)
        val testF: Array<Float> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf(1.0f, 2.0f, 3.0f), testF)
        val testD: Array<Double> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf(1.0, 2.0, 3.0), testD)
        val testB: Array<Boolean> = TransformerService.convert("yes, no, true, false, 1, 0")
        assertArrayEquals(arrayOf(true, false, true, false, true, false), testB)

        var testCS: Collection<String> = TransformerService.convert("1, 2, 3")
        assertArrayEquals(arrayOf("1", "2", "3"), testCS.toTypedArray())

        assertEquals(File("/tmp"), TransformerService.convert("/tmp"))
        assertEquals("/tmp", TransformerService.convert(File("/tmp")))

        TransformerService.convert<Collection<*>>(listOf(1, 2, 3))

        TransformerService.convert<Iterator<*>>(listOf(1, 2, 3))
        TransformerService.convert<Iterator<*>>("1,2,3")
        TransformerService.convert<Iterator<*>>(arrayOf(1,2,3))
    }

    @Test
    fun testMap() {
        val convert = mapOf<String, Any?>("1" to "1", "2" to 2, "3" to false)
        val result: Map<String, Any?> = TransformerService.convert(convert)
        assertEquals(convert, result)
    }
}