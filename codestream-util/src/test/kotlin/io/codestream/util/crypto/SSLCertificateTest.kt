package io.codestream.util.crypto

import org.junit.jupiter.api.Test


class SSLCertificateTest {




    @Test
    fun testGeneratePKCS10CSR() {
        val cert = SSLCertificate(
                cn="localhost",
                ou="codestream",
                org="codestream",
                locality ="nowhereia",
                state = "nowhereiastate",
                country = "SA"
        )

        val (keyPair, csr) = cert.pkcs10CSR()
        println(csr)
        println(keyPair.private)
        println(keyPair.public)

    }

    @Test
    fun testSelfSigned() {
        val cert = SSLCertificate(
                cn="localhost",
                ou="codestream",
                org="codestream",
                locality ="nowhereia",
                state = "nowhereiastate",
                country = "SA"
        )
        val (keyPair, certificate) = cert.selfSignedCertificate
        println(certificate)
        println(keyPair)
    }
}