package com.xfinance.xbank.repositories.accountrepo;

import com.xfinance.xbank.models.corebanking.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findBySortCodeAndAccountNumber(String sortCode, Long accountNumber);
	Optional<Account> findByAccountNumber(Long accountNumber);
}