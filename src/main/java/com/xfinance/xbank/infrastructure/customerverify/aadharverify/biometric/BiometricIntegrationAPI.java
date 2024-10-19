package com.xfinance.xbank.infrastructure.customerverify.aadharverify.biometric;

public interface BiometricIntegrationAPI {
	void captureBiometrics(CaptureHandler callback);
}