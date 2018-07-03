module io.codestream.runtime {

    requires io.codestream.di;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;


}
