import io.codestream.util.crypto.SimpleSecretStore;

open module io.codestream.api {

    requires io.codestream.di;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.scripting;
    requires semantic.version;


    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;


    exports io.codestream.doc;
    exports io.codestream.api;
    exports io.codestream.api.annotations;
    exports io.codestream.api.resources;
    exports io.codestream.api.services;
    exports io.codestream.api.events;
    exports io.codestream.api.metamodel;
    exports io.codestream.api.descriptor;


    uses io.codestream.api.CodestreamModule;
    uses io.codestream.api.CodestreamFactory;
    uses SimpleSecretStore;


}