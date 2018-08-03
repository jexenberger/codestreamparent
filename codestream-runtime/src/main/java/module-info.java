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

import io.codestream.api.CodestreamFactory;
import io.codestream.runtime.CodestreamRuntimeFactory;

open module io.codestream.runtime {

    //until ServiceLoader resolution works better
    exports io.codestream.runtime;

    requires io.codestream.di;
    requires io.codestream.api;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    requires java.scripting;

    requires semantic.version;

    requires snakeyaml;

    requires mvel2;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;



    uses io.codestream.api.CodestreamModule;
    provides CodestreamFactory with CodestreamRuntimeFactory;




}