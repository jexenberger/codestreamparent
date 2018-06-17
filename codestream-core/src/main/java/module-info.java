module codestream.core {

    requires codestream.di;

    requires transitive codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    requires java.scripting;
    requires org.codehaus.groovy.jsr223;

    requires semantic.version;

    requires jackson.annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.module.kotlin;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;


    exports io.codestream.core.api.annotations;
    exports io.codestream.core.metamodel;
    exports io.codestream.core.runtime.tree;



}