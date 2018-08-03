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
import io.codestream.di.api.addType
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PrototypeScopeTest {


    @Test
    fun testInstance() {
        val ctx = DefaultApplicationContext()

        addType<AnotherObject>(AnotherObject::class) to ctx

        val result = PrototypeScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), ctx)
        assertNotNull(result)
    }


    @Test
    fun testGet() {
        val (a, isNew) = PrototypeScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        val (b, isAlsoNew) = PrototypeScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        val (c, _) = PrototypeScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        assertTrue { isNew }
        assertTrue { isAlsoNew }
        assertFalse { a == b }
        assertFalse { a == c }
        assertFalse { b == c }
    }
}