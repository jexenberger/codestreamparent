module codestream.di.test {

    exports io.codestream.di.sample.repo;
    exports io.codestream.di.sample.model;

    requires transitive io.codestream.util;
    requires io.codestream.di;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;


}