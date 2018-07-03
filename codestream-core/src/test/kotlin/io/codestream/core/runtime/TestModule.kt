package io.codestream.core.runtime

import io.codestream.core.api.BasicModule

class TestModule : BasicModule("test","A test module") {

    init {
        create {
            add(SampleSimpleTask::class)
            add(ReallySimpleTask::class)
            add(SimpleGroupTask::class)
        }
    }

}