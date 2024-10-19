package com.xfinance.xbank.services.public_banking;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.amazonaws.secretsmanager.caching.SecretCache;

public class SecretManager implements RequestHandler<String, String> {
	private final SecretCache cache = new SecretCache();

	@Override public String handleRequest(String secretId, Context context) {
		final String secret = cache.getSecretString(secretId);
		// Use the secret, return success;
	}

	public static void getSecret(String secretName, String region) {
		String endpoint = ("secretsmanager." + region + ".amazonaws.com");
		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
		AWSSecretManagerClientBuilder clientBuilder = AWSSecretManagerClientBuilder.standard();
		clientBuilder.setEndpointConfiguration(config);
		AWSSecretsManager client = clientBuilder.build();

		String secret = null;
		ByteBuffer binarySecretData;

		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
		GetSecretValueResult getSecretValueResponse = null;
		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (RespurceNotFoundException e) {
			System.out.println("The requested secret " + secretName + " was not found.");
		} catch (InvalidRequestException e) {
			System.out.println("The request was invalid due to: " + e.getMessage());
		} catch (InvalidParamaterException e) {
			System.out.println("The request has invalid params: " + e.getMessage());
		}

		if (getSecretValueResponse == null) {
			return;
		}

		// Decrypted secret using the associated KMS CMK
		// Depending on whether the secret was a string or binary, one of these fields will be populated
		if (getSecretValueResponse.getSecretString() != null) {
			secret = getSecretValueResponse.getSecretString();
		} else {
			binarySecretData = getSecretValueResponse.getSecretBinary();
		}

		// TODO : Enter the code
		System.out.println("Secret Name : " + secretName + "\t Secret Value : " + secret + "\n");
	}

	
}