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

package io.codestream.util.transformation


object Transformations {


    fun toBoolean(o: String?): Boolean {
        return if (o == null) {
            false
        } else o.toString().equals("1", ignoreCase = true) ||
                o.toString().equals("yes", ignoreCase = true) ||
                o.toString().equals("true", ignoreCase = true) ||
                o.toString().equals("y", ignoreCase = true)

    }

    fun toBoolean(o: Char?): Boolean {
        return if (o == null) {
            false
        } else o == '1' || o == 'y' || o == 'Y'
    }

    fun toBoolean(o: Number?): Boolean {
        return if (o == null) {
            false
        } else o.toInt() == 1
    }


}