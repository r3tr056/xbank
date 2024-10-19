package com.xfinance.xbank.public_banking.installment_service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.stereotype.Service;

@Service
public class InstallmentService {
	@AutoWired
	private final InstallmentRepository installmentRepo;
	@AutoWired
	private final InstallmentMapper installmentMapper;
	@AutoWired
	private final CreditCardRepository creditCardRepo;
	@AutoWired
	private final CreditRepository creditRepo;
	@AutoWired
	private final AccountRepository accountRepo;
	@AutoWired
	private final CreditStatusRepository creditStatusRepo;

	@AutoWired
	public InstallmentService(InstallmentMapper installmentMapper, 
		InstallmentRepository installmentRepo, 
		CreditCardRepository creditCardRepo, 
		AccountRepository accountRepo, 
		CreditStatusRepository creditStatusRepo) {

		this.installmentMapper = installmentMapper;
		this.installmentRepo = installmentRepo;
		this.creditCardRepo = creditCardRepo;
		this.accountRepo = accountRepo;
		this.creditStatusRepo = creditStatusRepo;
	}

	public InstallmentOut create(InstallmentIn installmentIn) {

		Installment installment = new Installment();
		Credit credit = creditRepo.findById(installmentIn.getCreditId()).orElseThrow(() -> new RuntimeException("Credit Record not found."));
		Account srcAccount = accountRepo.findById(installmentIn.getAccountId()).orElseThrow(() -> new RuntimeException("Account not found"));

		if (!Objects.equals(srcAccount.getCurrencyType().getName(), installmentIn.getCurrency())) {
			throw new RuntimeException("Invalid source account currency");
		}

		BigDecimal installmentAmount = credit.getInstallmentAmount();
		if (srcAccount.getBalance().compareTo(installmentAmount) < 0) {
			throw new ApiException("Exception.notEnoughAccountBalance", null);
		}

		srcAccount.setBalance(srcAccount.getBalance().subtract(installmentAmount));
		credit.setBalancePaid(credit.getBalancePaid().add(installmentAmount));
		installment.setPayDate(Instant.now());
		installment.setCredit(credit);
		installment.setAmount(installmentAmount);

		if (credit.getBalancePaid().compareTo(credit.getTotalBalance()) >= 0) {
			credit.setCreditStatus(creditStatusRepo.findByCreditType(CreditStatus.CreditType.PAID));
			credit.setBalancePaid(credit.getTotalBalance());
		}

		return installmentRepo.save(installment);
	}

	public List<InstallemntOut> findAllByCreditId(Long id) {
		return installmentRepo.findAllByCredit_Id(id).stream().collect(Collectors.toList());
	}
}