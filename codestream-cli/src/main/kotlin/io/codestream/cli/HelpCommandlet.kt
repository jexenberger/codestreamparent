package io.codestream.cli

import io.codestream.core.api.Codestream
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Value
import java.io.File

class HelpCommandlet(
        @Inject
        val codestream: Codestream,
        @Value("task.ref")
        val task: String
) : Commandlet {

    override fun run() {



        codestream.modules
    }
}