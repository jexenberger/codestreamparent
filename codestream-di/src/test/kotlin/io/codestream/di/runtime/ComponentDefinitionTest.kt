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
import io.codestream.di.sample.SampleObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ComponentDefinitionTest {

    private val ctx  = DefaultApplicationContext()

    private val anotherObject = AnotherObject()

    @BeforeEach
    fun beforeAll() {
        SingletonScope.clear()
        InstanceScope.clear()
        bind(theInstance(anotherObject)) into ctx
    }

    @Test
    fun testCreate() {
        val def = ComponentDefinition<SampleObject>(ConstructorInjection(SampleObject::class), id(SampleObject::class))
        val result = def.instance(ctx)
        assertNotNull(result)
        assertEquals(anotherObject, result.anotherObject)
        assertEquals(anotherObject, result.injected)
    }
}