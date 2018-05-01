package io.codestream.di.sample

import io.codestream.di.annotation.Inject

class SampleObject(val anotherObject: AnotherObject) : Sample {

    override fun doStuff() = 999

    @Inject
    var injected: AnotherObject? = null

    var leaveMeAlone:String = "left alone"

}