package com.xfinance.xbank.repositories.transactionrepo;

import com.xfinance.xbank.models.corebanking.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findBySourceAccountIdOrderByInitiationDate(Long id);
}