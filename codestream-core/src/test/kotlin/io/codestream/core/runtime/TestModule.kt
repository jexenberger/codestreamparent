package io.codestream.runtime

import de.skuzzle.semantic.Version
import io.codestream.core.api.KotlinModule

class TestModule : KotlinModule("test","A test module", Version.create(1, 0, 0)) {

    init {
        create {
            add(SampleSimpleTask::class)
            add(ReallySimpleTask::class)
            add(SimpleGroupTask::class)
        }
    }

}