package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models;

import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.otp.OtpRes;

public class OtpResponseDetails {

	private OtpRes otpRes;
	private String xml;

	public OtpResponseDetails(String xml, OtpRes res) {
		this.otpRes = res;
		this.xml = xml;
	}

	public OtpRes getOtpRes() { return this.otpRes; }
	public String getXml() { return this.xml; }
}