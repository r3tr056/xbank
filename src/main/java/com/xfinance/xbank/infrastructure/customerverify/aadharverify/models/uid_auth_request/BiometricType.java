
package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="bioMetricType")
@XmlEnum
public enum BiometricType {
	// Finger Minutiae
	FMR,
	// Finger Image
	FIR,
	// Iris Image
	IIR;

	public String value() {
		return name();
	}

	public static BiometricType fromValue(String v) {
		return valueOf(v);
	}
}