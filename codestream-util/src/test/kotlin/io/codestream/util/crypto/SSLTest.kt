package io.codestream.util.crypto

import io.codestream.util.system
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class SSLTest {

    @Test
    fun testCtx() {
        val f = "${system.tempDir}/${UUID.randomUUID()}"
        File(f).deleteOnExit()
        val store = SSL.defaultStoreAndKey("localhost", f)
        val ctx = SSL.ctx(store.store, store.store)
        assertNotNull(ctx)
        assertNotNull(store["localhost"])
    }
}