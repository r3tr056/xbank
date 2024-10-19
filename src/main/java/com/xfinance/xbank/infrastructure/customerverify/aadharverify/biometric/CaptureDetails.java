package com.xfinance.xbank.infrastructure.customerverify.aadharverify.biometric;

import java.awt.Image;

/**
 * CaptureDetails represents biometrics capture details
 * 
 * @author packluke@XCloud
 */
public class CaptureDetails {
	Image image;
	byte[] isoFeatureSet;

	public CaptureDetails(Image image, byte[] isoFeatureSet) {
		this.image = image;
		this.isoFeatureSet = isoFeatureSet;
	}

	public Image getImage() {
		return image;
	}

	public byte[] getIsoFeatureSet() {
		return isoFeatureSet;
	}
}