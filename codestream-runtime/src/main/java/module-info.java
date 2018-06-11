module codestream.runtime {

    requires codestream.di;

    requires transitive codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;
    requires org.codehaus.groovy.jsr223;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;

    uses io.codestream.di.api.DIModule;

}
