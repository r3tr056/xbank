package com.xfinance.xbank.models.customerdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerAddress {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="pin_code")
	private String pinCode;
	@Column(name="care_of")
	private String careOf;
	@Column(name="building")
	private String building;
	@Column(name="street")
	private String street;
	@Column(name="landmark")
	private String landmark;
	@Column(name="village")
	private String village;
	@Column(name="po_name")
	private String poName;
	@Column(name="subdistrict")
	private String subdistrict;
	@Column(name="district")
	private String district;
	@Column(name="state")
	private String state;
	@Column(name="address_match_strategy")
	private MatchingStrategy addressMatchStrategy;

	@Column(name="full_address")
	private String fullAddress;
	@Column(name="local_full_address")
	private String localFullAddress;
	@Column(name="full_address_match_strategy")
	private MatchingStrategy fullAddressMatchStrategy;
	@Column(name="full_address_match_value")
	private int fullAddressMatchValue;
	@Column(name="local_full_address_match_value")
	private int localFullAddressMatchValue;

	@OneToOne(mappedBy="customer_address")
	@JsonIgnore
	private Customer customer;
}