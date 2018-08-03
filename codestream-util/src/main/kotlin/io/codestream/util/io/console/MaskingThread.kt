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

package io.codestream.util.io.console

/**
 * Taken from http://www.cse.chalmers.se/edu/course/TDA602/Eraserlab/pwdmasking.html
 */

internal class MaskingThread(prompt: String) : Thread() {
    @Volatile
    private var stop: Boolean = false
    private val echochar = '*'

    init {
        print(prompt)
    }

    /**
     * Begin masking until asked to stop.
     */
    override fun run() {

        val priority = Thread.currentThread().priority
        Thread.currentThread().priority = Thread.MAX_PRIORITY

        try {
            stop = true
            while (stop) {
                print("\u0008" + echochar)
                try {
                    // attempt masking at this rate
                    Thread.sleep(1)
                } catch (iex: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return
                }

            }
        } finally { // restore the original priority
            Thread.currentThread().priority = priority
        }
    }

    /**
     * Instruct the thread to stop masking.
     */
    fun stopMasking() {
        this.stop = false
    }
}