package io.codestream.cli

import io.codestream.api.Codestream
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



    }
}