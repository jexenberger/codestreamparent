package io.codestream.util.rest

import java.net.Proxy

interface HttpProxy {
    fun toProxy(): Proxy
}