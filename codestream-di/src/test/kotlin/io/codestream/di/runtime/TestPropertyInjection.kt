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

import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject
import io.codestream.di.api.DefaultApplicationContext
import io.codestream.di.api.addInstance
import io.codestream.di.api.id
import io.codestream.util.mutablePropertyByName
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.test.assertEquals

class TestPropertyInjection {

    @BeforeEach
    fun setUp() {
        InstanceScope.clear()
        SingletonScope.clear()
    }

    @Test
    fun testRun() {
        val ctx = DefaultApplicationContext();
        val injected = AnotherObject()

        addInstance(injected) into ctx

        val property:KMutableProperty<*> = SampleObject::class.mutablePropertyByName("injected")
        val sampleObject = SampleObject(AnotherObject(), nullable = null)
        val propertyInjection = PropertyInjection(id("test"),  property, sampleObject)
        propertyInjection.run(ctx)
        assertEquals(injected, sampleObject.injected)
    }
}