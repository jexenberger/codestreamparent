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
import io.codestream.di.api.DefaultApplicationContext
import io.codestream.di.api.TypeId
import io.codestream.di.api.addInstance
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InstanceScopeTest {

    @Test
    fun testInstance() {
        val ctx = DefaultApplicationContext()
        val instance = AnotherObject()

        addInstance(instance) into ctx

        val result = InstanceScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), InstanceInjection(instance), ctx)
        assertNotNull(result)
    }


    @Test
    fun testGet() {
        val instance = AnotherObject()
        InstanceScope.add(TypeId(AnotherObject::class), instance)
        val (a, isNew) = InstanceScope.getOrCreate(TypeId(AnotherObject::class), InstanceInjection(instance), DefaultApplicationContext())
        val (b, isAlsoNew) = InstanceScope.getOrCreate(TypeId(AnotherObject::class), InstanceInjection(instance), DefaultApplicationContext())
        val (c, _) = InstanceScope.getOrCreate(TypeId(AnotherObject::class), InstanceInjection(instance), DefaultApplicationContext())
        assertFalse { isNew }
        assertFalse { isAlsoNew }
        assertTrue { a == b }
        assertTrue { a == c }
        assertTrue { b == c }
    }

}