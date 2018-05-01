package io.codestream.di.runtime

import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject
import io.codestream.di.api.ApplicationContext
import io.codestream.di.api.DependencyTarget
import io.codestream.di.api.bind
import io.codestream.di.api.theType
import io.codestream.util.mutablePropertyByName
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DependencyResolverTest {

    private val ctx = DefaultApplicationContext()

    @Test
    fun testResolve() {
        val sampleObject = SampleObject(AnotherObject())

        bind(theType<SampleObject>(SampleObject::class)) into ctx
        bind(theType<AnotherObject>(AnotherObject::class)) into ctx

        val next:KMutableProperty<*> = sampleObject::class.mutablePropertyByName("injected")
        val callSite = DependencyTarget(next)
        val result = DependencyResolver.getDependency(callSite, ctx)
        assertNotNull(result)
        assertTrue { result is InjectionDependency }
    }
}