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

import io.codestream.di.annotation.Eval
import io.codestream.di.api.*
import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject
import io.codestream.util.OS
import io.codestream.util.mutablePropertyByName
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertNotNull

class TestEvalDependency {

    @BeforeEach
    fun setUp() {
        InstanceScope.clear()
        SingletonScope.clear()
    }



    private val ctx = DefaultApplicationContext()

    @Test
    fun testResolve() {
        val sampleObject = SampleObject(AnotherObject(), nullable = null)

        bind(theType<SampleObject>(SampleObject::class)) into ctx
        bind(theType<AnotherObject>(AnotherObject::class)) into ctx

        val next: KMutableProperty<*> = sampleObject::class.mutablePropertyByName("evaled")
        val callSite = DependencyTarget(id("test"), SampleObject::class, next)
        val handler = EvalDependency()
        val findAnnotation = next.findAnnotation<Eval>()!!
        val result = handler.resolve<OS>(findAnnotation, callSite, ctx)
        assertNotNull(result)

    }
}