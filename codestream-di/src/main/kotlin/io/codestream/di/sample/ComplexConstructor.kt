package io.codestream.di.sample

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.annotation.Value
import io.codestream.di.annotation.WiredConstructor

class ComplexConstructor(val name:String) {

    @WiredConstructor
    constructor(
            @Qualified("sample") name:String,
            @Qualified test:String,
            sampleObject: SampleObject) : this(name) {
        this.test = test
        this.sampleObject = sampleObject
    }

    var test:String = "hello"

    var sampleObject: SampleObject? = null

    @Inject
    var anotherObject: AnotherObject? = null

    @Value("other.value")
    var otherVal:String? = "test"

    val result:String get() = "$name $test"

}