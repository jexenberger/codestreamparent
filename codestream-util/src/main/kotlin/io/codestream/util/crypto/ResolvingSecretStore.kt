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

package io.codestream.util.crypto

class ResolvingSecretStore(var primary:SimpleSecretStore) : SimpleSecretStore {

    val stores:MutableSet<SimpleSecretStore> = mutableSetOf(primary)

    override fun get(keyName: String): Secret? {
        for (store in stores) {
            val result = store[keyName]
            if (result != null) {
                return result
            }
        }
        return null
    }

    override fun set(keyName: String, secret: Secret) {
        primary[keyName] = secret
    }



}