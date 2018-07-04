open module io.codestream.core {

    requires io.codestream.di;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    requires java.scripting;
    requires org.codehaus.groovy;
    requires org.codehaus.groovy.jsr223;

    requires semantic.version;

    requires snakeyaml;

    requires jmustache;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;


    exports io.codestream.core.api;
    exports io.codestream.core.api.annotations;
    exports io.codestream.core.api.resources;
    exports io.codestream.core.api.services;
    exports io.codestream.core.api.events;
    exports io.codestream.core.api.metamodel;
    exports io.codestream.core.api.descriptor;


    uses io.codestream.core.api.CodestreamModule;



}