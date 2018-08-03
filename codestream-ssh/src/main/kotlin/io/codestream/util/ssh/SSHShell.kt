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

package io.codestream.util.ssh

import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.Session
import java.io.InputStream
import java.io.OutputStream

class SSHShell(val session: Session, val prompt: String = "$", val eol: String = "\n", val emulation: String?) {

    val channel: ChannelShell
    val input: InputStream
    val output: OutputStream

    init {
        channel = getExecChannel()
        input = channel.inputStream
        output = channel.outputStream
    }

    private fun getExecChannel(): ChannelShell {
        val channel = session.openChannel("shell") as ChannelShell
        channel.connect()
        emulation?.let { channel.setPtyType(it) }
        return channel
    }

    fun exec(cmd: String, handler: (String) -> Unit) {
        drainBuffer()
        output.write(cmd.toByteArray())
        output.write("\n".toByteArray())
        output.flush()
        var output = ""
        var buffer = ""
        do {
            output = drainBuffer()
            buffer += output
        } while (!output.equals(""))
        buffer = buffer.trim()
        buffer.splitToSequence(eol).filter { it.endsWith(prompt) }.forEach {
            handler(it)
        }
    }

    private fun drainBuffer(): String {
        val arr = ByteArray(input.available())
        input.read(arr)
        val data = String(arr)
        return data
    }

    fun disconnect() {
        channel.disconnect()
    }

}