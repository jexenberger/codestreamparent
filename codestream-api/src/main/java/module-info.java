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

import io.codestream.util.crypto.SimpleSecretStore;

open module io.codestream.api {

    requires io.codestream.di;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;
    requires semantic.version;


    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;


    exports io.codestream.doc;
    exports io.codestream.api;
    exports io.codestream.api.annotations;
    exports io.codestream.api.resources;
    exports io.codestream.api.services;
    exports io.codestream.api.events;
    exports io.codestream.api.metamodel;
    exports io.codestream.api.descriptor;


    uses io.codestream.api.CodestreamModule;
    uses io.codestream.api.CodestreamFactory;
    uses SimpleSecretStore;


}