
package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models;

import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request.DataType;
import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request.BiometricType;
import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request.BiometricPosition;
import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request.MatchingStrategy;
import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.common.types.Meta;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used for collecting various information from the application so that it can be used
 * to create Pid and Auth objects
 * 
 * @author packluke
 */
public class KYCCollectedAuthData implements Serializable {

	private static final long serialVersionUID = -969857695481409943L;

	private String language;

	private String uid;
	private String name;
	private String lname;
	private MatchingStrategy nameMatchStrategy;
	private int nameMatchValue;
	private int localNameMatchValue;

	private String gender;
	private String dob;
	private String dobType;
	private String phoneNo;
	private String email;

	private String age;

	private String pinCode;
	private String careOf;
	private String building;
	private String street;
	private String landmark;
	private String locality;
	private String village;
	private String poName;
	private String subdistrict;
	private String district;
	private String state;
	private MatchingStrategy addressMatchStrategy;

	private String fullAddress;
	private String localFullAddress;
	private MatchingStrategy fullAddressMatchStrategy;
	private int fullAddressMatchValue;
	private int localFullAddressMatchValue;

	private String staticPin;
	private string dynamicPin;

	private String biometricPosition;
	private String biometricType;

	private DataType pidType;

	private List<BiometricData> biometrics;

	private Meta deviceMetaData;

	public String getDob() { return dob; }
	public void setDob(String dob) { this.dob = dob; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }

	public String getPinCode() { return pinCode; }
	public void setPinCode() { this.pincode = pincode; }

	public static class BiometricData {
		BiometricPosition position;
		BiometricType type;
		byte[] biometricContent;

		public BiometricData(BiometricPosition pos, BiometricType type, byte[] content) {
			super();
			this.position = pos;
			this.type = type;
			this.biometricContent = content;
		}

		public byte[] getBiometricContent() { return this.biometricContent; }
		public BiometricPosition getBiometricPosition() { return this.position; }
		public BiometricType getBiometricType() { return this.type; }

		@Override
		public String toString() {
			return this.position.toString();
		}
	}

}