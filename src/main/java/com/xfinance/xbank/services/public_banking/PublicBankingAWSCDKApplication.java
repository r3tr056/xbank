package com.xfinance.xbank.services.public_banking;

import software.amazon.awscdk.App;

public class PublicBankingAWSCDKApplication {

	public static void main(final String[] args) {
		App app = new App();
		new CdkStack(app, "com_xfinance_xbank_services_publicbanking_110");
		app.synth();
	}
}