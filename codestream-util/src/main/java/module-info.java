module io.codestream.util {
    exports io.codestream.util;
    exports io.codestream.util.io;
    exports io.codestream.util.rest;
    exports io.codestream.util.transformation;

    requires java.base;
    requires java.scripting;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;


    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;

}