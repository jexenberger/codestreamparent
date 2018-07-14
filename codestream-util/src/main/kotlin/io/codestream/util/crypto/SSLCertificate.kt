package io.codestream.util.crypto

import io.codestream.util.toDate
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.BasicConstraints
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.KeyUsage
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringWriter
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Security
import java.security.cert.X509Certificate
import java.time.LocalDate
import java.util.*
import javax.security.auth.x500.X500Principal


class SSLCertificate(
        val cn: String,
        val ou: String,
        val org: String,
        val locality: String,
        val state: String,
        val country: String = Locale.getDefault().isO3Country,
        val algorithm: String = "RSA",
        val signature: String = "SHA256WithRSA",
        val validity: Long = 730,
        val keySize: Int = 2048

) {


    val selfSignedCertificate get() = generate()

    private fun buildPrincipalMap(): Map<String, String> {
        return linkedMapOf(
                "OU" to ou,
                "CN" to cn,
                "O" to org,
                "ST" to state,
                "C" to country
        ).filterValues { it.isNotBlank() }
    }

    private fun buildPrincipalString() = buildPrincipalMap().entries.map { "${it.key}=${it.value}" }.joinToString(", ")




    fun generate(signingCertificate: KeyPair = generateKeyPair(algorithm, keySize),
                 certificateType: CertificateType = CertificateType.DigitalSignature): Pair<PrivateKey, X509Certificate> {
        val builder = JcaX509v3CertificateBuilder(
                X500Name(buildPrincipalString()), //here intermedCA is issuer authority
                BigInteger.valueOf(Random().nextInt().toLong()), Date(),
                LocalDate.now().plusDays(validity).toDate(),
                X500Name(buildPrincipalString()), signingCertificate.getPublic())
        builder.addExtension(Extension.keyUsage, true, KeyUsage(certificateTypes[certificateType]!!))
        builder.addExtension(Extension.basicConstraints, false, BasicConstraints(false))
        val endUserCert = JcaX509CertificateConverter().getCertificate(builder
                .build(JcaContentSignerBuilder(signature).build(signingCertificate.getPrivate())))!!// private key of signing authority , here it is signed by intermedCA
        return signingCertificate.private to endUserCert
    }

    fun pkcs10CSR(keyPair: KeyPair = generateKeyPair(algorithm, keySize)): Pair<KeyPair, String> {
        val p10builder = JcaPKCS10CertificationRequestBuilder(X500Principal(buildPrincipalString()), keyPair.public)
        val csBuilder = JcaContentSignerBuilder("SHA256withRSA")
        val signer = csBuilder.build(keyPair.private)
        val csr = p10builder.build(signer)
        val writer = StringWriter()
        val pem = PemWriter(writer)
        pem.writeObject(PemObject("REQUEST", csr.encoded))
        return keyPair to writer.toString()
    }

    fun generateKeyPair(alg: String, keySize: Int): KeyPair {
        var keyPairGenerator: KeyPairGenerator? = null
        keyPairGenerator = KeyPairGenerator.getInstance(alg)
        keyPairGenerator.initialize(keySize)
        return keyPairGenerator.generateKeyPair()
    }


    companion object {

        private val certificateTypes = mapOf<CertificateType, Int>(
                CertificateType.CA to KeyUsage.keyCertSign,
                CertificateType.DigitalSignature to KeyUsage.digitalSignature
        )

        init {
            //Security.addProvider(BouncyCastleProvider());
        }
    }
}