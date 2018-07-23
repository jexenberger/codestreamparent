module io.codestream.ssh {

    requires io.codestream.api;

    requires transitive io.codestream.util;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires jsch;

    //until intelliJ figures this out
    requires static org.junit.jupiter.engine;
    requires static org.junit.jupiter.api;
    requires static kotlin.test.junit;

    requires static sshd.core;
    requires static sshd.scp;

}