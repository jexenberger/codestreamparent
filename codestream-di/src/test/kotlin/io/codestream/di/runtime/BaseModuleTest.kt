package io.codestream.di.runtime

import io.codestream.di.sample.AnotherObject
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class BaseModuleTest {

    @Test
    internal fun testDefine() {
        val module = SampleBaseModule()
        val ctx = module.ctx()
        val instance: AnotherObject? = ctx.get(AnotherObject::class)
        assertNotNull(instance)
    }
}