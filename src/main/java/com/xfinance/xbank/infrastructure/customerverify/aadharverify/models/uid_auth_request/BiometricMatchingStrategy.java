package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="matchingStrategy")
@XmlEnum
public enum BiometricMatchingStrategy {
	// Exact Match
	E,
	// Partial Match
	P,
	// Fuzzy Match
	F;

	public String value() {
		return name();
	}

	public static BiometricMatchingStrategy fromValue(String v) {
		return valueOf(v);
	}
}