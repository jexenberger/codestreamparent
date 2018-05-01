package io.codestream.di.runtime

import io.codestream.di.sample.AnotherObject
import io.codestream.di.api.ApplicationContext
import io.codestream.di.api.TypeId
import io.codestream.di.api.addType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SingletonScopeTest {

    @BeforeEach
    fun setUp() {
        SingletonScope.clear()
    }

    @Test
    fun testInstance() {
        val ctx = DefaultApplicationContext()

        addType<AnotherObject>(AnotherObject::class) into ctx

        val result = SingletonScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), ctx)
        assertNotNull(result)
    }


    @Test
    fun testGet() {
        val (a, isNew) = SingletonScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        val (b, isAlsoNew) = SingletonScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        val (c, _) = SingletonScope.getOrCreate<AnotherObject>(TypeId(AnotherObject::class), ConstructorInjection(AnotherObject::class), DefaultApplicationContext())
        assertTrue { isNew }
        assertFalse { isAlsoNew }
        assertTrue { a == b }
        assertTrue { a == c }
        assertTrue { b == c }
    }
}