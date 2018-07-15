package io.codestream.di.sample

import io.codestream.di.annotation.Eval
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.util.OS

class SampleObject(val anotherObject: AnotherObject,
                   @Qualified("test")
                   val test:String = "hello",
                   var aVar:String? = "aVar",
                   val nullable:String?) : Sample {

    override fun doStuff() = 999

    @Inject
    var injected: AnotherObject? = null

    var optionalProperty: String? = "hello world"

    @Eval("_os")
    var evaled:OS? = null

    var leaveMeAlone:String = "left alone"

    val readOnly:String = "this is readonly"

}