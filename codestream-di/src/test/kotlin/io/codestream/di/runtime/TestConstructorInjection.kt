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