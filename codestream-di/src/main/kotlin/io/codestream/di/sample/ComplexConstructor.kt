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

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.annotation.Value
import io.codestream.di.annotation.WiredConstructor

class ComplexConstructor(val name:String) {

    @WiredConstructor
    constructor(
            @Qualified("sample") name:String,
            @Qualified test:String,
            sampleObject: SampleObject) : this(name) {
        this.test = test
        this.sampleObject = sampleObject
    }

    var test:String = "hello"

    var sampleObject: SampleObject? = null

    @Inject
    var anotherObject: AnotherObject? = null

    @Value("other.value")
    var otherVal:String? = "test"

    val result:String get() = "$name $test"

}