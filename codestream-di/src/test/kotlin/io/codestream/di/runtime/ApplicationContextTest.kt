package io.codestream.di.runtime

import io.codestream.di.api.*
import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.ComplexConstructor
import io.codestream.di.sample.SampleObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApplicationContextTest {

    private val ctx = DefaultApplicationContext()

    @Test
    fun testRegisterBean() {
        val sampleObject = SampleObject(AnotherObject())

        bind { id, ctx -> AnotherObject() } withId id(AnotherObject::class) into ctx
        bind(theInstance(sampleObject)) into ctx

        val instance = ctx.get<SampleObject>(SampleObject::class)
        assertEquals(sampleObject, instance);
    }

    @Test
    fun testDoesntExist() {
        val result = ctx.get<String>("test")
        assertNull(result)
    }

    @Test
    fun testRegisterByName() {
        bind { a,b -> AnotherObject() } withId id("qwerty") into ctx
        val result = ctx.get<AnotherObject>("qwerty")
        assertNotNull(result)
    }

    @Test
    fun testRegisterAsPrototype() {
        bind { a,b -> AnotherObject() } withId id("prototype") toScope ScopeType.prototype.name into ctx
        val a = ctx.get<AnotherObject>("prototype")
        val b = ctx.get<AnotherObject>("prototype")
        assertFalse { a == b }
    }

    @Test
    fun testRunComplexType() {
        val ctx = DefaultApplicationContext();
        ctx.setValue("other.value", "look another value")

        bind(theType<SampleObject>(SampleObject::class)) into ctx
        bind(theType<AnotherObject>(AnotherObject::class)) into ctx
        bind(theType<ComplexConstructor>(ComplexConstructor::class)) into ctx

        bind(theInstance("hello")) withId id("sample") into ctx
        bind(theInstance("world")) withId id("test") into ctx

        val result = ctx.get(ComplexConstructor::class) as ComplexConstructor?
        assertEquals("hello world", result?.result)
        assertEquals("look another value", result?.otherVal)
        assertNotNull(result?.sampleObject)
        assertNotNull(result?.anotherObject)
    }


}