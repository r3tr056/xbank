package com.xfinance.xbank.infrastructure.customerverify.aadharverify;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DataDecryptor {

	private static final int PUBLIC_KEY_SIZE = 294;
	private static final int EID_SIZE = 32;
	private static final int SECRET_KEY_SIZE = 256;
	private static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
	private static final String SEC_PROVIDER = "BC";
	private static final String DIGEST_ALGO = "SHA-256";
	private static final String MASKING_FUNCTION = "MGF1";
	private static final int VECTOR_SIZE = 16;
	private static final int HMAC_SIZE = 32;
	private static final int BLOCK_SIZE = 128;
	private static final byte[] HEADER_DATA = "VERSION_1.0".getBytes();
	private static final String SIGNATURE_TAG = "Signature";
	private static final String MEC_TYPE = "DOM";

	private KeyStore.PrivateKeyEntry privateKey;
	private String publicKeyFile;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public DataDecryptor(String keyStoreFile, char[] keyStorePassword, String publickeyFile) {

		this.privateKey = getKeyFromFile(keyStoreFile, keyStorePassword);
		this.publickeyFile = publickeyFile;

		if (privateKey == null) {
			throw new RuntimeException("Key could not be read for digital signature. Please check the value of signature, alias and signature password, and restart the `customerverify.AadharVerify` service");
		}
	}

	public byte[] decrypt(byte[] data) throws Exception {

		if (data == null || data.length == 0) {
			throw new Exception("byte array data cannot be null or blank array");
		}

		ByteArraySpliter arrSpliter = new ByteArraySpliter(data);

		byte[] secretKey = decryptSecretKeyData(arrSpliter.getEncryptedSecretkey(), arrSpliter.getIv(), privateKey.getPrivateKey());

		byte[] plainData = decryptData(arrSpliter.getEncryptedData(), arrSpliter.getIv(), secretKey);

		boolean result = validateHash(plainData);
		if (!result) {
			throw new Exception("Integrity Validation Failed : The original data at client side and the decrypted data at server side does not match");
		}

		return trimHMAC(plainData);
	}

	
}