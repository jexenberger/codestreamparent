package io.codestream.di.sample

import io.codestream.di.annotation.Eval
import io.codestream.di.annotation.Inject
import io.codestream.util.OS

class SampleObject(val anotherObject: AnotherObject) : Sample {

    override fun doStuff() = 999

    @Inject
    var injected: AnotherObject? = null

    @Eval("_os")
    var evaled:OS? = null

    var leaveMeAlone:String = "left alone"

}