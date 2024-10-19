package com.xfinance.xbank.models.public_banking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

import com.xfinance.xbank.models.public_banking.PaymentRequestStatus;

@Entity
@Table(name="payment_request_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestData {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="date")
	private Instant created_at;

	@Column(name="stored_data_id")
	private Long stored_data_id;

	@Column(name="stored_data")
	private StoredData stored_data;

	@Column(name="payment_req_status")
	private PaymentRequestStatus payment_req_status;
}