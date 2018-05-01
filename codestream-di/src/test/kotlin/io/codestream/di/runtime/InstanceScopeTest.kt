package io.codestream.di.runtime

import io.codestream.di.sample.AnotherObject
import io.codestream.di.api.ApplicationContext
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