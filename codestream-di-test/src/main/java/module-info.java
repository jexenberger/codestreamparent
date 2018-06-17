module codestream.di.test {

    exports io.codestream.di.sample.repo;
    exports io.codestream.di.sample.model;

    requires transitive codestream.util;
    requires codestream.di;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;
    requires org.codehaus.groovy.jsr223;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;


}