package io.codestream.di.api

import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject
import io.codestream.di.runtime.InstanceScope
import io.codestream.di.runtime.SingletonScope
import io.codestream.util.mutableProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DependencyTargetTest {

    private val property: KMutableProperty<*> = SampleObject::class.mutableProperties.first { it.name == "injected" }

    private val constructor: KFunction<SampleObject> = SampleObject::class.constructors.iterator().next()
    private val id: TypeId = TypeId(SampleObject::class)
    private val type: KClass<*> = SampleObject::class

    @BeforeEach
    fun setUp() {
        InstanceScope.clear()
        SingletonScope.clear()
    }

    @Test
    fun testConstructorAndInit() {
        val dependencyTarget = DependencyTarget(id, type, property)
        assertNotNull(dependencyTarget.property)
        assertNull(dependencyTarget.parameter)

        val constructorDependencyTarget = DependencyTarget(id, type, constructor.parameters[0])
        assertNotNull(constructorDependencyTarget.parameter)
        assertNull(constructorDependencyTarget.property)
    }

    @Test
    fun testAnnotatedElement() {
        val dependencyTarget = DependencyTarget(id, type, property)
        assertEquals(property, dependencyTarget.annotatedElement)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals(parameter, constructorDependencyTarget.annotatedElement)
    }

    @Test
    fun testTargetType() {
        val dependencyTarget = DependencyTarget(id, type, property)
        assertEquals(AnotherObject::class, dependencyTarget.targetType)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals(AnotherObject::class, constructorDependencyTarget.targetType)
    }

    @Test
    fun testName() {
        val dependencyTarget = DependencyTarget(id, type, property)
        assertEquals("injected", dependencyTarget.name)

        val parameter = constructor.parameters[0]
        val constructorDependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals("anotherObject", constructorDependencyTarget.name)
    }
}