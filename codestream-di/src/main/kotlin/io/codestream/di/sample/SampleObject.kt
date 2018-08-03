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

package io.codestream.di.sample

import io.codestream.di.annotation.Eval
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.util.OS

class SampleObject(val anotherObject: AnotherObject,
                   @Qualified("test")
                   val test:String = "hello",
                   var aVar:String? = "aVar",
                   val nullable:String?) : Sample {

    override fun doStuff() = 999

    @Inject
    var injected: AnotherObject? = null

    var optionalProperty: String? = "hello world"

    @Eval("_os")
    var evaled:OS? = null

    var leaveMeAlone:String = "left alone"

    val readOnly:String = "this is readonly"

}