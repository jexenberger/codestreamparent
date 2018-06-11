package io.codestream.di.event

class SampleEventHandler : EventHandler<SampleEvent> {

    var called = false
    override fun onEvent(event: SampleEvent) {
        println(event)
        called = true
    }
}