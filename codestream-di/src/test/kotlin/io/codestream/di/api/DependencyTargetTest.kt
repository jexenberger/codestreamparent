package io.codestream.di.api

import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject
import io.codestream.di.runtime.InstanceScope
import io.codestream.di.runtime.SingletonScope
import io.codestream.util.mutableProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DependencyTargetTest {

    private val property: KMutableProperty<*> = SampleObject::class.mutableProperties.iterator().next()

    private val constructor: KFunction<SampleObject> = SampleObject::class.constructors.iterator().next()

    @BeforeEach
    fun setUp() {
        InstanceScope.clear()
        SingletonScope.clear()
    }

    @Test
    fun testConstructorAndInit() {
        val dependencyTarget = DependencyTarget(property)
        assertNotNull(dependencyTarget.property)
        assertNull(dependencyTarget.parameter)

        val constructorDependencyTarget = DependencyTarget(constructor.parameters[0])
        assertNotNull(constructorDependencyTarget.parameter)
        assertNull(constructorDependencyTarget.property)
    }

    @Test
    fun testAnnotatedElement() {
        val dependencyTarget = DependencyTarget(property)
        assertEquals(property, dependencyTarget.annotatedElement)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(parameter)
        assertEquals(parameter, constructorDependencyTarget.annotatedElement)
    }

    @Test
    fun testTargetType() {
        val dependencyTarget = DependencyTarget(property)
        assertEquals(AnotherObject::class, dependencyTarget.targetType)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(parameter)
        assertEquals(AnotherObject::class, constructorDependencyTarget.targetType)
    }

    @Test
    fun testName() {
        val dependencyTarget = DependencyTarget(property)
        assertEquals("injected", dependencyTarget.name)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(parameter)
        assertEquals("anotherObject", constructorDependencyTarget.name)
    }
}