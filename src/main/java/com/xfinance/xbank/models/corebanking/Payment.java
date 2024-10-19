package com.xfinance.xbank.models.corebanking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="beneficiary_bank_account")
	private Account beneficiaryAccount;

	@ManyToOne
	@JoinColumn(name="source_currency_type_id")
	private CurrencyType srcCurrencyType;

	@Column(name="balance")
	private BigDecimal balance;

	@Column(name="date")
	private Instant date;
}