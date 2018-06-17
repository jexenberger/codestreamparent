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
        val sampleObject = SampleObject(AnotherObject())
        val propertyInjection = PropertyInjection(id("test"),  property, sampleObject)
        propertyInjection.run(ctx)
        assertEquals(injected, sampleObject.injected)
    }
}