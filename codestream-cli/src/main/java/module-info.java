open module io.codestream.cli {

    requires jdk.unsupported;
    requires io.codestream.di;

    requires io.codestream.util;
    requires io.codestream.api;
    requires io.codestream.runtime;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;

    requires kotlin.argparser;
    requires semantic.version;
    //Snakeyaml tries to load dates as java.sql.Date
    requires java.sql;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;


}
