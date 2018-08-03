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

import org.junit.jupiter.api.Test
import java.io.IOException


class RequestTest {



    @Test
    fun testHeader() {
        val request = Request(
                url = "https://api.iextrading.com",
                path = "1.0/stock/aapl/batch",
                validateHostName = false,
                validateSSL = false,
                contentType = "application/json"
        )
        request.headers(
                "test1" to "1",
                "test2" to "2"
        )
    }

    /*
    @Test
    fun testGet() {

        val request = Request(
                url = "http://localhost:4444",
                path = "test/test",
                validateHostName = false,
                validateSSL = false,
                contentType = "application/json"
        )
        try {
            val result = request
                    .parm("range", "1m")
                    .parm("last", "10")
                    .get()

            println(result)
        } catch (e: IOException) {
            //ignore it did go through and fire off the request
            e.printStackTrace()
        }
    }

    @Test
    fun testPost() {


        val request = Request(
                url = "http://localhost:4444",
                path = "test/test",
                contentType = "application/json",
                validateHostName = false,
                validateSSL = false)
        request.parms(
                "types" to "quote,news,chart",
                "range" to "1m",
                "last" to "10"
        )
        try {
            println(request.post())
        } catch (e: IOException) {
            //ignore it did go through and fire off the request
            e.printStackTrace()
        }

    }*/
}