package com.xfinance.xbank.services.transactionservice;

import com.xfinance.xbank.services.transactionservice.ACTION;
import com.xfinance.xbank.models.corebanking.Account;
import com.xfinance.xbank.models.corebanking.Transaction;
import com.xfinance.xbank.repositories.accountrepo.AccountRepository;
import com.xfinance.xbank.repositories.transactionrepo.TransactionRepository;

import java.util.List;

public interface TransactionService {
	public boolean makeTransfer(TransactionInput transactionInput);

	public void updateAccountBalance(Account account, double amount, ACTION action);

	public boolean isAmountAvailable(double amount, double accountBalance);
}