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

import javax.script.ScriptEngineFactory;

module io.codestream.util {
    exports io.codestream.util;
    exports io.codestream.util.io;
    exports io.codestream.util.csv;
    exports io.codestream.util.script;
    exports io.codestream.util.crypto;
    exports io.codestream.util.rest;
    exports io.codestream.util.transformation;
    exports io.codestream.util.io.console;

    requires java.base;
    requires java.scripting;

    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;
    requires mvel2;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;



}