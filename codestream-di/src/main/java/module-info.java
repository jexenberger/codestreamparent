import io.codestream.di.api.DIModule;
import io.codestream.di.api.DefinableContextFactory;
import io.codestream.di.runtime.DefaultDefinableContextFactory;
import io.codestream.di.runtime.DefaultModule;

module codestream.di {

    exports io.codestream.di.annotation;
    exports io.codestream.di.api;
    exports io.codestream.di.sample;

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

    uses DIModule;
    uses DefinableContextFactory;

    provides DIModule with DefaultModule;
    provides DefinableContextFactory with DefaultDefinableContextFactory;
}