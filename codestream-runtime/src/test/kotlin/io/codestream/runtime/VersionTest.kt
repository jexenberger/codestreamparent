package io.codestream.runtime

import de.skuzzle.semantic.Version
import org.junit.jupiter.api.Test

class VersionTest {

    @Test
    internal fun testNullVersion() {
        Version.create(999999999, 999999999, 999999999)

        val TwoDotOhOh = Version.create(2, 0, 0)
        val OneDotOhOh = Version.create(1, 0, 0)
        val versions = listOf(OneDotOhOh, TwoDotOhOh).sortedByDescending { it }
        println(versions)
    }
}