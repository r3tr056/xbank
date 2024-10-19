package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models;

import java.io.Serializable;
import java.util.List;

public class KYCCollectedBfdData implements Serializable {

	private static final long serialVersionUID = -969857695481409943L;

	private String uid;
	private List<BiometricData> biometrics;
	private Meta deviceMetaData;

	public DeviceCollectedBfdData(String uid, List<BiometricData> biometrics, Meta deviceMetaData) {
		super();
		this.uid = uid;
		this.biometrics = biometrics;
		this.deviceMetaData = deviceMetaData;
	}

	public List<BiometricData> getBiometrics() { return biometrics; }
	public Meta getDeviceMetaData() { return deviceMetaData; }
	public String getUid() { return uid; }

	public static class BiometricData {

		FingerPosition position;
		byte[] biometricContent;
		int nfiq;

		public BiometricData(FingerPosition position, byte[] biometricContent, int nfiq) {
			super();
			this.position = position;
			this.biometricContent = biometricContent;
			this.nfiq = nfiq;
		}

		public byte[] getBiometricContent() { return this.biometricContent; }
		public FingerPosition getPosition() { return this.position; }
		public int getNfiq() { return this.nfiq; }
	}
}