
package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="BiometricPosition")
@XmlEnum
public enum BiometricPosition {

	LEFT_IRIS,
    RIGHT_IRIS,
    BOTH_IRIS,
    LEFT_INDEX,
    LEFT_LITTLE,
    LEFT_MIDDLE,
    LEFT_RING,
    LEFT_THUMB,
    RIGHT_INDEX,
    RIGHT_LITTLE,
    RIGHT_MIDDLE,
    RIGHT_RING,
    RIGHT_THUMB,
    BOTH_THUMBS,
    LEFT_SLAP,
    RIGHT_SLAP,
    UNKNOWN;

    public String value() {
    	return name();
    }

    public static BiometricPosition fromValue(String v) {
    	return valueOf(v);
    }
}