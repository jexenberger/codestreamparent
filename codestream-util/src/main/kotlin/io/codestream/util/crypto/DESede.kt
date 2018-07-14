package io.codestream.util.crypto

import java.security.Key
import javax.crypto.KeyGenerator


class DESede : BaseCipherHandler {
    override val algorithm: String
        get() = ALGORITHM


    companion object {
        private val ALGORITHM = "DESede"

        fun generateKey(): Key {
            val key = KeyGenerator.getInstance(ALGORITHM)
            key.init(168)
            return key.generateKey()

        }

    }
}


