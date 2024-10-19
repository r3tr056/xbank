package com.xfinance.xbank.services.accountservice;

import com.xfinance.xbank.models.Account;
import com.xfinance.xbank.repositories.AccountRepository;
import com.xfinance.xbank.repositories.TransactionRepository;
import com.xfinance.xbank.utils.CodeGenerator;

import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * Account Service
 */
@Service
public class AccountService {
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public AccountService(AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public Account getAccount(String sortCode, String accountNumber) {
        Optional<Account> account = accountRepo.findBySortCodeAndAccountNumber(sortCode, accountNumber);
        account.ifPresent(value -> value.setTransations(transactionRepo.findBySourceAccountIdOrderByIntiationDate(value.getId())));
        return account.orElse(null);
    }

    public Account getAccount(String accountNumber) {
        Optional<Account> account = accountRepo.findByAccountNumber(accountNumber);
        return account.orElse(null);
    }

    public Account createAccount(String bankName, String ownerName) {
        CodeGenerator codeGenerator = new CodeGenerator();
        Account newAccount = new Account(bankName, ownerName, codeGenerator.generateSortCode(), codeGenerator.generateAccountNumber(), 0.00);
        return accountRepo.save(newAccount);
    }
}