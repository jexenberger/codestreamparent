package io.codestream.di.runtime

import io.codestream.di.sample.AnotherObject
import io.codestream.di.api.ApplicationContext
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