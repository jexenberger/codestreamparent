module codestream.core {

    requires codestream.di;

    requires transitive codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    requires java.scripting;
    requires org.codehaus.groovy.jsr223;

    requires semantic.version;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;


    exports io.codestream.core.api.annotations;
    exports io.codestream.core.metamodel;
    exports io.codestream.core.runtime.tree;



}