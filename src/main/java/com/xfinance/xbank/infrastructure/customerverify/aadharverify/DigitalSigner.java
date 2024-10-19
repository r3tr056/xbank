package com.xfinance.xbank.infrastructure.customerverify.aadharverify;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


/**
 * DigitalSigner class provides utility methods to digitally sign XML document.
 * This implementation uses .p12 file as a source of signer's digital certificates
 * In production HSM should be used for signing
 */
public class DigitalSigner {

	private KeyStore.PrivateKeyEntry privkeyEntry;

	private static final String MEC_TYPE = "DOM";
	private static final String WHOLE_DOC_URI = "";
	private static final String KEY_STORE_TYPE = "PKCS12";

	public DigitalSigner(String keyStoreFile, char[] keyStorePassword, String alias) {
		this.privkeyEntry = getKeyFromKeyStore(keyStoreFile, keyStorePassword, alias);
		if (this.privkeyEntry == null) {
			throw new RuntimeException("Key could not be read for digital signature. Please check value of signature, alias and signature password and restart the `CustomerVerify.AadharVerify.AadharAuthService`");
		}
	}

	public String signXML(String xmlDoc, boolean includeKeyInfo) throws RuntimeException {
		Security.addProvider(new BouncyCastleProvider());

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document inputDoc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlDoc)));

			// Sign the input XML's DOM document
			Document signedDoc = sign(inputDoc, includeKeyInfo);

			// Convert the signedDoc to XML String
			StringWriter stringWriter = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(signedDoc), new StreamResult(stringWriter));

			return stringWriter.getBuffer().toString();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error while signing the XML document", ex);
		}
	}

	private Document sign(Document xmlDoc, boolean includeKeyInfo) throws Exception {
		// XML Signature factory
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance(MEC_TYPE);
		// The reference object, reading the whole document for signing
		Reference ref = fac.newReference(WHOLE_DOC_URI, fac.newDigestMethod(DigestMethod.SHA256, null), Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null);
		// Create the SignedInfo
		SignedInfo sInfo = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), fac.newSignatureMethod(SignatureMethod.RSA_SHA256, null), Collections.singletonList(ref));

		if (privkeyEntry == null) {
			throw new RuntimeException("Private Key Could not be read for digital signing.");
		}

		X509Certificate x509Cert = (X509Certificate) privkeyEntry.getCertificate();
		if (includeKeyInfo) { KeyInfo kInfo = getKeyInfo(x509Cert, fac); }
		DOMSignContext dsc = new DOMSignContext(this.privkeyEntry.getPrivateKey(), xmlDoc.getDocumentElement());
		XMLSignature signature = fac.newXMLSignature(sInfo, includeKeyInfo ? kInfo : null);
		signature.sign(dsc);

		Node node = dsc.getParent();
		return node.getOwnerDocument();
	}

	@SuppressWarnings("unchecked")
	private KeyInfo getKeyInfo(X509Certificate cert, XMLSignatureFactory fac) {
		// Create Key Info containing the X509Data
		KeyInfoFactory kifac = fac.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert.getSubjectX500Principal().getName());
		x509Content.add(cert);
		X509Data xd = kifac.newX509Data(x509Content);
		return kifac.newKeyInfo(Collections.singletonList(xd));
	}

	private KeyStore.PrivateKeyEntry getKeyFromKeyStore(String keyStoreFile, char[] keyStorePassword, String alias) {
		// Load the KeyStore and get the signing key and certificate
		FileInputStream keyFileStream = null;
		try {
			KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
			keyFileStream = new FileInputStream(keyStoreFile);
			ks.load(keyFileStream, keyStorePassword);

			KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(keyStorePassword));
			return entry;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		} finally {
			if (keyFileStream != null) {
				try {
					keyFileStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}