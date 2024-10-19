package com.xfinance.xbank.controllers.coreservices.transaction_rest_controller;

import com.xfinance.xbank.constants.ACTION;
import com.xfinance.xbank.models.Account;
import com.xfinance.xbank.services.accountservice.AccountService;
import com.xfinance.xbank.services.transactionservice.TransactionService;
import com.xfinance.xbank.controllers.coreservices.transaction_rest_controller.request_format.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.xfinance.xbank.constants.constants.*;

@RestController
@RequestMapping("api/v1")
public class TransactionRestController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);

	private final AccountService accountService;
	private final TransactionService transactionService;

	@AutoWired
	public TransactionRestController(AccountService accountService, TransactionService transactionService) {
		this.accountService = accountService;
		this.transactionService = transactionService;
	}

	/**
	 * Check if a transaction is complete
	 * 
	 * @param TransactionInput Transaction Input Request
	 * @return ResponseEntity
	 */
	@PostMapping(value="/transactions", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> makeTransfer(@Valid @RequestBody TransactionInput transactionInput) {
		if (InputValidator.isSearchTransactionValid(transactionInput)) {
			boolean isComplete = transactionService.makeTransfer(transactionInput);
			return new ResponseEntity<>(isComplete, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(INVALID_TRANSACTION, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Withdraw REST method
	 * 
	 * @param WidthdrawInput : Widthdraw Input Request
	 * @return ResponseEntity
	 */
	@PostMapping(value="/withdraw", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> withdraw(@Valid @RequestBody WidthdrawInput withdrawInput) {
		logger.debug("Triggered AccountRestController.withdrawInput");

		// Validate input
		if (InputValidator.isSearchCriteriaValid(withdrawInput)) {
			// Attempt to retrive the account information
			Account account = accountService.getAccount(withdrawInput.getSortCode(), withdrawInput.getAccountNumber());

			// Return the account details, or warn that no account was found for given input
			if (account == null) {
				return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
			} else {
				if (transactionService.isAmountAvailable(withdrawInput.getAmount(), account.getCurrentBalance())) {
					transactionService.updateAccountBalance(account, withdrawInput.getAmount(), ACTION.WITHDRAW);
					return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
				}
				return new ResponseEntity<>(INSUFFICIENT_ACCOUNT_BALANCE, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Deposit REST method
	 * 
	 * @param DepositInput : Deposit Input Request
	 * @return ResponseEntity
	 */
	@PostMapping(value="/deposit", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deposit(@Valid @RequestBody DepositInput depositInput) {
		logger.debug("Triggered AccountRestController.depositInput");

		// Validate input
		if (InputValidator.isAccountNoValid(depositInput.getTargetAccountNo())) {
			// Attempt to retrive the account information
			Account account = accountService.getAccount(depositInput.getTargetAccountNo());
			if (account == null) {
				return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
			} else {
				transactionService.updateAccountBalance(account, depositInput.getAmount(), ACTION.DEPOSIT);
				return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return errors;
	}
}