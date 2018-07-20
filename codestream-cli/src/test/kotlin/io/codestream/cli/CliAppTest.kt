package io.codestream.cli

import com.xenomachina.argparser.ArgParser
import org.junit.jupiter.api.Test

class CliAppTest {
    @Test
    internal fun testRun() {
        CliApp(ArgParser(arrayOf("evaluate", "sys::echo","-Ivalue=hello world"))).run()
    }
}