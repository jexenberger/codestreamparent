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