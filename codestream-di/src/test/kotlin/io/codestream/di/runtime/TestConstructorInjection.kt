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
import io.codestream.di.sample.NoArgsConstructorDefault
import io.codestream.di.sample.SampleObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestConstructorInjection {

    @BeforeEach
    fun setUp() {
        InstanceScope.clear()
        SingletonScope.clear()
    }


    @Test
    fun testRun() {
        val injector = ConstructorInjection<SampleObject>(SampleObject::class)
        val ctx = DefaultApplicationContext();
        val injected = AnotherObject()

        addInstance(injected) into ctx

        val instance: SampleObject = injector.run(id("test"), ctx) as SampleObject
        assertEquals(injected, instance.anotherObject)
        assertEquals("hello", instance.test)
        assertEquals("aVar", instance.aVar)
        assertNull(instance.nullable)
    }

    @Test
    fun testRunOverrideOptional() {
        val injector = ConstructorInjection<SampleObject>(SampleObject::class)
        val ctx = DefaultApplicationContext();
        val injected = AnotherObject()

        addInstance(injected) into ctx
        addInstance("override") withId StringId("test") into ctx

        val instance: SampleObject = injector.run(id("test"), ctx) as SampleObject
        assertEquals(injected, instance.anotherObject)
        assertEquals("override", instance.test)
        assertEquals("aVar", instance.aVar)
        assertNull(instance.nullable)
    }

    @Test
    fun testRunResolveToNull() {
        val injector = ConstructorInjection<SampleObject>(SampleObject::class)
        val ctx = DefaultApplicationContext();
        val injected = AnotherObject()

        addInstance(injected) into ctx

        val instance: SampleObject = injector.run(id("test"), ctx) as SampleObject
        assertEquals(injected, instance.anotherObject)
    }

    @Test
    fun testRunComplexWiredConstructor() {
        val injector = ConstructorInjection<ComplexConstructor>(ComplexConstructor::class)
        val ctx = DefaultApplicationContext();

        addType<SampleObject>(SampleObject::class) into ctx
        addType<AnotherObject>(AnotherObject::class) into ctx
        addInstance("hello") withId id("sample") into ctx
        addInstance("world") withId id("test") into ctx

        val result = injector.run(id("test"), ctx) as ComplexConstructor
        assertEquals("hello world", result.result)
        assertNotNull(result.sampleObject)
    }

    @Test
    fun testDefaultNoArgsConstructor() {
        val injector = ConstructorInjection<NoArgsConstructorDefault>(NoArgsConstructorDefault::class)
        val ctx = DefaultApplicationContext();
        val result = injector.run(id("test"), ctx) as NoArgsConstructorDefault
        result.name = "test"
        assertEquals("test", result.name)
    }
}