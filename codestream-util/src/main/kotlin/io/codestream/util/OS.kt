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

package io.codestream.util

import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


val system: OS
    get() = OS.os()

enum class OS(val unixVariant: Boolean, val rootDir: String, vararg val keys: String) {

    Windows(false, "C:\\", "windows"),
    Linux(true, "/", "nux"),
    OSX(true, "/", "mac", "darwin"),
    Unix(true, "/", "nix"),
    AIX(true, "/", "aix"),
    Solaris(true, "/", "sunos", "solaris");

    val optimizedExecutor: ExecutorService by lazy {
        val cpus = Runtime.getRuntime().availableProcessors()
        val threads = cpus * 3
        val service = Executors.newFixedThreadPool(threads)
        ExecutorServiceWrapper(service)
    }

    val version: String
        get() = System.getProperty("os.version")

    val newLine: String
        get() = System.getProperty("line.separator")

    val user: String
        get() = System.getProperty("user.name")

    val architecture: String
        get() = System.getProperty("os.arch")

    var pwd: String
        get() = System.getProperty("user.dir")
        set(value) {
            System.setProperty("user.dir", value)
        }

    val homeDir: String
        get() = System.getProperty("user.home")

    val tempDir: String
        get() = System.getProperty("java.io.tmpdir")

    val pathSeperator: String
        get() = System.getProperty("path.separator")

    fun pathString(vararg parts: String) = parts.joinToString(pathSeperator)

    fun parsePathPath(path: String) = path.split(pathSeperator)

    fun fsString(vararg parts: String, absolute: Boolean = false): String {
        val path = parts.joinToString(this.fileSeperator)
        return if (absolute) "$rootDir$path" else path
    }


    val fileSeperator: String
        get() = System.getProperty("file.separator")

    val env: Map<String, String>
        get() = System.getenv()

    val props: Properties
        get() = System.getProperties()

    fun exec(dir: File = File(pwd),
             timeout: Long = 60,
             timeUnit: TimeUnit = TimeUnit.MINUTES,
             cmd: Array<String>): Pair<Int, String> {
        val buffer = mutableListOf<String>()
        val res = exec(
                cmd = cmd,
                dir = dir,
                timeUnit = timeUnit,
                timeout = timeout)
        {
            buffer += it
        }
        return res to buffer.joinToString(System.getProperty("line.separator"))
    }

    fun exec(cmd: Array<String>,
             dir: File = File(System.getProperty("user.dir")),
             timeout: Long = 60,
             timeUnit: TimeUnit = TimeUnit.MINUTES,
             handler: (String) -> Unit): Int {
        val builder = ProcessBuilder(cmd.toList())
        builder.environment().putAll(env)
        var process = builder
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .directory(dir)
                .start()
        val bufferedReader = process.inputStream.bufferedReader()
        var line: String?
        do {
            line = bufferedReader.readLine()
            line?.let { handler(it) }
        } while (line != null)
        bufferedReader.forEachLine {
            handler(it)
        }
        val completed = process.waitFor(timeout, timeUnit)
        if (!completed) {
            return -1
        }
        val result = process.exitValue()
        return result
    }

    fun shell(
            cmd: String,
            dir: String = this.pwd,
            timeout: Long = 60
    ): Pair<Int, String> {
        val buffer = mutableListOf<String>()
        val res = shell(
                cmd = cmd,
                dir = dir,
                timeout = timeout
        ) {
            buffer += it
        }
        return res to buffer.joinToString(System.getProperty("line.separator"))
    }

    fun shell(
            cmd: String,
            dir: String = this.pwd,
            timeout: Long = 60,
            handler: (String) -> Unit
    ): Int {
        val command = if (unixVariant) {
            arrayOf("/bin/sh", "-c", cmd)
        } else {
            arrayOf("cmd", "-c", "\"${cmd}")
        }
        return exec(
                cmd = command,
                dir = File(dir),
                timeout = timeout,
                handler = handler
        )
    }


    companion object {
        @JvmStatic
        fun os(): OS {
            val osKey = System.getProperty("os.name").toLowerCase()
            OS.values()
                    .filter { os -> os.keys.find { osKey.contains(it) } != null }
                    .forEach { return it }
            throw IllegalStateException("unrecognised OS: '$osKey'")
        }
    }


}

