package io.codestream.sample

import io.codestream.api.KotlinModule

class SampleModule : KotlinModule("sample", "sample test module") {


    init {
        this.create {
            add(Greeter::class)
        }
    }


}