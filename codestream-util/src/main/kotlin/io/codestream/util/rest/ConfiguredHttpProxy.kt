/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.util.rest

import io.codestream.util.system
import java.io.File
import java.net.Authenticator
import java.net.InetSocketAddress
import java.net.PasswordAuthentication
import java.net.Proxy
import java.util.*

data class ConfiguredHttpProxy(val server: String,
                               val port: Int = 8080,
                               val user: String? = null,
                               val password: String? = null) : HttpProxy {


    override fun toProxy(): Proxy {
        if (user != null && password != null) {
            Authenticator.setDefault(object : Authenticator() {

                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(user, password.toCharArray())
                }
            })
        }
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(server, port))
    }


    companion object {

        var defaultProxyPath = "${system.homeDir}/.cs/proxy.properties"

        var globalProxyConfigured: ConfiguredHttpProxy? = loadProxy()

        fun loadProxy(path: String? = null): ConfiguredHttpProxy? {
            val p = Properties()
            val fileToLoad = path?.let { it } ?: defaultProxyPath
            val file = File(fileToLoad)
            if (!file.exists()) {
                return null
            }
            p.load(file.reader())
            val server = p["server"]?.toString()
            val port = p["port"]?.toString()?.toInt() ?: 8080
            val user = p["user"]?.toString() ?: ""
            val password = p["password"]?.toString() ?: ""
            return server?.let { ConfiguredHttpProxy(it, port, user, password) }
        }

    }

}