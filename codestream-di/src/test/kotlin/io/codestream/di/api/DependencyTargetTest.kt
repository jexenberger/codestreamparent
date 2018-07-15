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
import kotlin.test.*

class DependencyTargetTest {

    private val property: KMutableProperty<*> = SampleObject::class.mutableProperties.first { it.name == "injected" }
    private val nullable: KMutableProperty<*> = SampleObject::class.mutableProperties.first { it.name == "evaled" }
    private val optional: KMutableProperty<*> = SampleObject::class.mutableProperties.first { it.name == "optionalProperty" }

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

    @Test
    fun testOptional() {
        val parameter = constructor.parameters[1]
        val dependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals("test", dependencyTarget.name)
        assertTrue { dependencyTarget.isOptional }

    }

    @Test
    fun testOptionalProperty() {
        val dependencyTarget = DependencyTarget(id, type, optional)
        assertEquals("optionalProperty", dependencyTarget.name)
        assertFalse { dependencyTarget.isOptional }

    }

    @Test
    fun testNotOptional() {
        val parameter = constructor.parameters[0]
        val dependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals("anotherObject", dependencyTarget.name)
        assertFalse { dependencyTarget.isOptional }
    }


    @Test
    fun testNullable() {
        val parameter = constructor.parameters[2]
        val dependencyTarget = DependencyTarget(id, type, parameter)
        assertEquals("aVar", dependencyTarget.name)
        assertTrue { dependencyTarget.isNullable }

    }

    @Test
    fun testNullableProperty() {
        val dependencyTarget = DependencyTarget(id, type, nullable)
        assertEquals("evaled", dependencyTarget.name)
        assertTrue { dependencyTarget.isNullable }

    }


}