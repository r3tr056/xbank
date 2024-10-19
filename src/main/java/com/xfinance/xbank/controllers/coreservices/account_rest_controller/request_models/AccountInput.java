package com.xfinance.xbank.controllers.coreservices.account_rest_controller.request_models;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class AccountInput {

	@NotBlank(message="Sort code is mandatory.")
	private String sortCode;

	@NotBlank(message="Account number is mandatory.")
	private Long accountNumber;

	public AccountInput() {}

	public String getSortCode() {
		return this.sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public Long getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "AccountInput{" + "sortCode='" + sortCode + '\'' + ", accountNumber='" + accountNumber + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == 0) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccountInput that = (AccountInput) o;
		return Objects.equals(sortCode, that.sortCode) && Object.equals(accountNumber, that.accountNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sortCode, accountNumber);
	}
}