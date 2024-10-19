package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models;

/**
 * AUAData class represents the information that typically needs to be 
 * transferred by an authentication device to AUA Server.
 * 
 * @author packluke@XCloud
 */
public class AUAData {
	String uid;
	String terminalId;
	byte[] encryptedPid;
	byte[] encryptedHmac;
	byte[] encryptedSkey;
	byte[] hashedDemoXML;
	String cerificateIdentifier;

	public AUAData(
		String uid,
		String terminalId,
		byte[] encryptedPid,
		byte[] encryptedHmac,
		byte[] encryptedSkey,
		byte[] hashedDemoXML,
		String cerificateIdentifier
	) {
		this.uid = uid;
		this.terminalId = terminalId;
		this.encryptedPid = encryptedPid;
		this.encryptedHmac = encryptedHmac;
		this.encryptedSkey = encryptedSkey;
		this.hashedDemoXML = hashedDemoXML;
		this.cerificateIdentifier = cerificateIdentifier;
	}

	public String getUid() {
		return uid;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public byte[] getEncryptedPid() {
		return encryptedPid;
	}

	public byte[] getEncryptedHmac() {
		return encryptedHmac;
	}

	public byte[] getEncryptedSkey() {
		return encryptedSkey;
	}

	public byte[] getHashedDemoXML() {
		return hashedDemoXML;
	}

	public String getCertificateIdentifier() {
		return cerificateIdentifier;
	}
}