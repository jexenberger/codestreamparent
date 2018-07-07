package io.codestream.sample

import io.codestream.core.api.KotlinModule
import io.codestream.core.api.CodestreamModule

class SampleModule : KotlinModule("sample", "sample test module"), CodestreamModule {


    init {

        val jClass = this::class.java
        jClass.getResourceAsStream("/")
        println(jClass.module.descriptor.version())
        this.create {
            add(Greeter::class)
        }
    }


}