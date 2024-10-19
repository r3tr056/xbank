package com.xfinance.xbank.controllers.coreservices.transaction_rest_controller.request_models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class TransactionInput {
	private AccountInput sourceAccount;
	private AccountInput targetAccount;

	@Positive(message="Transfer amount must be Positive")
	@Min(value=1, message="Amount must be larger than 1")
	private double amount;

	private String reference;

	@Min(value=-90, message="Latitude must be between -90 and 90")
	@Max(value=90, message="Latitude must be between -90 and 90")
	private Double lat;

	@Min(value=-180)
	@Max(value=180)
	private Double lng;

	public TransactionInput() {}

	public AccountInput getSourceAccount() {
		return sourceAccount;
	}

	public AccountInput getTargetAccount() {
		return targetAccount;
	}

	public void setSourceAccount(AccountInput srcAcc) {
		this.sourceAccount = srcAcc;
	}

	public void setTargetAccount(AccountInput targetAcc) {
		this.targetAccount = targetAcc;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount() {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String ref) {
		this.reference = ref;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "TransactionInput{sourceAccount=" + sourceAccount.toString() + ", targetAccount=" + targetAccount.toString() + ", amount=" + amount.toString() + ", reference=" + reference.toString() + ", lat=" + lat.toString() + ", lng=" + lng.toString() + "}";
	}
}