import io.codestream.api.CodestreamFactory;
import io.codestream.api.CodestreamModule;
import io.codestream.runtime.CodestreamRuntimeFactory;

open module io.codestream.core {

    requires io.codestream.di;
    requires io.codestream.api;

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
    requires static kotlin.test.junit;



    uses io.codestream.api.CodestreamModule;
    provides CodestreamFactory with CodestreamRuntimeFactory;




}