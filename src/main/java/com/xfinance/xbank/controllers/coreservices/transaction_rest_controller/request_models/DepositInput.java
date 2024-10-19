package com.xfinance.xbank.controllers.transaction_rest_controller.request_models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class DepositInput {

	@NotBlank(message="Target account no is mandatory.")
	private Long targetAccountNumber;

	@Positive(message="Transfer amount must be Positive.")
	private double amount;

	public DepositInput() {}

	public Long getTargetAccountNumber() { return this.targetAccountNumber; }
	public void setTargetAccountNumber(Long targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }

	public double getAmount() { return this.amount; }
	public void setAmount(double amount) { this.amount = amount; }

	@Override
	public String toString() {
		return "DepositInput{" + "targetAccountNumber='" + targetAccountNumber + '\'' + ", amount='" + amount + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DepositInput that = (DepositInput) o;
		return Objects.equals(targetAccount, that.targetAccountNumber) && Objects.equals(amount, that.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetAccountNumber, amount);
	}
}