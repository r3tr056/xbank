package com.xfinance.xbank.models.public_banking.third_party_apps;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.xfinance.xbank.models.corebanking.Payment;

@Entity
@Table(name="third_party_processed_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdPartyPayment extends Payment {

	@Column(name="invoice_data")
	private InvoiceData invoice_data;

	@Column(name="invoice_data_id")
	private String invoivce_data_id;

	@Column(name="accounted")
	private Boolean accounted;

	@Column(name="info_data")
	private String info_data;
}