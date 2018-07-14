import io.codestream.api.CodestreamModule;

open module io.codestream.samplemodule {



    requires io.codestream.api;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;

    uses io.codestream.api.CodestreamModule;
    provides CodestreamModule with io.codestream.sample.SampleModule;

}