package com.xfinance.xbank.services.paymentservice;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

	@AutoWired
	private final PaymentRepository paymentRepo;
	@AutoWired
	private final CurrencyTypeConversionService currencyTypeConverter;
	@AutoWired
	private final ForeignMonetaryExachageService forexService;
	@AutoWired
	private final PaymentMapper paymentMapper;

	@AutoWired
	public PaymentService(PaymentRepository paymentRepo, 
		CurrencyTypeConversionService currencyTypeConverter, 
		AccountRepository accountRepo, 
		PaymentMapper paymentMapper) {

		this.paymentMapper = paymentMapper;
		this.paymentRepo = paymentRepo;
		this.currencyTypeConverter = currencyTypeConverter;
		this.accountRepo = accountRepo;
	}

	
	public PaymentResponse create(@NotNull PaymentRequest paymentReq) {
		Account srcAccount = accountRepo.findByNumberAndRemovedFalse(paymentRepo.getSourceAccountNumber()).orElseThrow(() -> new RuntimeException("Source bank account not found"));
		CurrencyType srccurrencyType = currencyTypeConverter.findByName(paymentReq.getSourceCurrencyType()).orElseThrow(() -> new RuntimeException("Source currency not found"));
		Balance bal = srcAccount.getBalance().stream().filter(e -> e.getCurrentType() == srccurrencyType).findFirst().orElseThrow(() -> new RuntimeException("Source bank account has no balance"));
		bal.setBalance(bal.getBalance().add(paymentReq.getBalance()));

	}

	public List<PaymentResponse> findAllByAccountId(Long accountId) {}

	public List<PaymentResponse> findAll() {}
}