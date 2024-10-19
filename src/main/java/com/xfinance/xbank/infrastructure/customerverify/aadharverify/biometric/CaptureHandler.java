package com.xfinance.xbank.infrastructure.customerverify.aadharverify.biometric;

public interface CaptureHandler {
	
	/**
	 * Applications should implement this method to handle the captured biometrics
	 * 
	 * @param CaptureDetails : Biometric capture details
	 */
	void onCapture(CaptureDetails details);
}