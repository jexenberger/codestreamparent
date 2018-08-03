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

package io.codestream.di.runtime

import io.codestream.di.api.*
import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.ComplexConstructor
import io.codestream.di.sample.SampleObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApplicationContextTest {

    private val ctx = DefaultApplicationContext()

    @Test
    fun testRegisterBean() {
        val sampleObject = SampleObject(AnotherObject(), nullable = "test")

        bind { _, _ -> AnotherObject() } withId id(AnotherObject::class) into ctx
        bind(theInstance(sampleObject)) into ctx

        val instance = ctx.get<SampleObject>(SampleObject::class)
        assertEquals(sampleObject, instance);
    }

    @Test
    fun testDoesntExist() {
        val result = ctx.get<String>("test")
        assertNull(result)
    }

    @Test
    fun testRegisterByName() {
        bind { _,_ -> AnotherObject() } withId id("qwerty") into ctx
        val result = ctx.get<AnotherObject>("qwerty")
        assertNotNull(result)
    }

    @Test
    fun testRegisterAsPrototype() {
        bind { _,_ -> AnotherObject() } withId id("prototype") toScope ScopeType.prototype.name into ctx
        val a = ctx.get<AnotherObject>("prototype")
        val b = ctx.get<AnotherObject>("prototype")
        assertFalse { a == b }
    }

    @Test
    fun testRunComplexType() {
        val ctx = DefaultApplicationContext()
        ctx.setValue("other.value", "look another value")

        bind(theType<SampleObject>(SampleObject::class)) into ctx
        bind(theType<AnotherObject>(AnotherObject::class)) into ctx
        bind(theType<ComplexConstructor>(ComplexConstructor::class)) into ctx

        bind(theInstance("hello")) withId id("sample") into ctx
        bind(theInstance("world")) withId id("test") into ctx

        val result = ctx.get(ComplexConstructor::class) as ComplexConstructor?
        assertEquals("hello world", result?.result)
        assertEquals("look another value", result?.otherVal)
        assertNotNull(result?.sampleObject)
        assertNotNull(result?.anotherObject)
    }


}