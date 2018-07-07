import io.codestream.core.api.CodestreamModule;

open module io.codestream.samplemodule {



    requires io.codestream.core;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test;
    requires static kotlin.test.junit;

    uses io.codestream.core.api.CodestreamModule;
    provides CodestreamModule with io.codestream.sample.SampleModule;

}