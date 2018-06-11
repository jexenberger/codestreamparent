package io.codestream.di.event

import io.codestream.di.sample.SampleObject
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class EventDispatcherTest {

    @Test
    internal fun testRegisterAndPublish() {
        val publisher = EventDispatcher()
        val handler = SampleEventHandler()
        publisher.register(handler)
        publisher.publish(SampleEvent("hello"))
        assertTrue { handler.called }
    }
}