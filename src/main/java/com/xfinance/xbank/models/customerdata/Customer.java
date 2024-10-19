package com.xfinance.xbank.models.customerdata;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="customers", uniqueConstraints={
	@UniqueConstraint(columnNames="customer_id"),
	@UniqueConstraint(columnNames="customer_bank_tie_id")
	@UniqueConstraint(columnNames="customer_account_number")
})
public class Customer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long customer_id;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long customer_bank_tie_id;

	@NotBlank
	@Size(max=255)
	private CustomerNameData customer_name;
	@NotBlank
	@Size(max=20)
	private CustomerDobData customer_dob;
	@NotBlank
	@Size(max=2)
	private CustomerGenderData customer_gender_data;

	@ManyToOne
	private CustomerAddress customer_address;
	@ManyToOne
	private CustomerContactDetails customer_contact;
	@ManyToOne
	private CustomerOccupation customer_occupation;
	@ManyToOne
	private CustomerMarriageImage customer_married_img;
	@ManyToOne
	private CustomerHeir customer_heir;

	@NotBlank
	private Integer customer_length_res;
	@NotBlank
	private Integer customer_since;
	@NotBlank
	private Integer customer_last_sub;

	@ManyToOne(fetch=FetchType.LAZY)
	private CustomerActivity customer_active;
	@ManyToOne(fetch=FetchType.LAZY)
	private CustomerActivity customer_last_activity;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinTable(name="customer_activity_data", joinColumns=@JoinColumn(name="customer_last_updated_id"), inverseJoinColumn=@JoinColumn(name="customer_last_updated_id"))
	private CustomerActivity customer_last_updated;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinTable(name="customer_biometric_data", joinColumns=@JoinColumn(name="customer_id"), inverseJoinColumn=@JoinColumn(name="biometric_data_id"))
	private CustomerBiometricData cust_biometric_data;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumn=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>();


	public Customer() {}

	public Customer(
		String customer_name, 
		String customer_dob, 
		String customer_sex_id,
		CustomerAddress customer_address,
		CustomerContactDetails customer_contact_details,
		CustomerOccupation customer_occupation,
		CustomerMarrigeImage customer_married_img,
		CustomerHeir customer_heir,

		Integer customer_len_res,
		Integer customer_since,
		Integer customer_last_sub,

		CustomerActivity customer_active,
		CustomerActivity customer_last_activity,
		CustomerActivity customer_last_updated,

		CustomerBiometricData customer_biometric_dat
	) {
		this.customer_name = customer_name;
		this.customer_dob = customer_dob;
		this.customer_sex_id = customer_sex_id;
		try {
			// verify and finalize customer_address
			// verify and finalize customer_contact_details
			// verify and finalize customer_occupation
			// verify and finalize customer_married_img
			// verify and finalize customer_heir
			customer_address.verifyAndFinalizeDetails();
			customer_contact_details.verifyAndFinalizeDetails();
			customer_occupation.verifyAndFinalizeDetails();
			customer_married_img.verifyAndFinalizeDetails();
			customer_heir.verifyAndFinalizeDetails();\
			this.customer_address = customer_address;
			this.customer_contact = customer_contact_details;
			this.customer_occupation = customer_occupation;
			this.customer_married_img = customer_married_img;
			this.customer_heir = customer_heir;
			// verify and finalize customer_active details
			// verify and finalize customer_last_activity details
			// verify and finalize customer_last_updated details
			customer_active.verifyAndFinalizeDetails();
			customer_last_activity.verifyAndFinalizeDetails();
			customer_last_updated.verifyAndFinalizeDetails();
			this.customer_active = customer_active;
			this.customer_last_activity = customer_last_activity;
			this.customer_last_updated = customer_last_updated;
			// Customer Biometric data
			customer_biometric_dat.verifyAndFinalizeDetails();
			this.cust_biometric_data = customer_biometric_dat;
		} catch (Exception ex) {

		}
		
	}

	public Long getCustomerId() {return customer_id;}
	public void setCustomerId(Long customer_id) {this.customer_id = customer_id;}

	public String getCustomerName() {return customer_name;}
	public void setCustomerName(String customer_name) {this.customer_name = customer_name;}

	public String getCustomerDob() {return customer_dob;}
	public void setCustomerDob(String customer_dob) {this.customer_dob = customer_dob;}

	public String getCustomerSexId() {return customer_sex_id;}
	public void setCustomerSexId(String customer_sex_id) {this.customer_sex_id = customer_sex_id;}

	public CustomerAddress getCustomerAddress() { return customer_address; }
	public void setCustomerAddress(CustomerAddress customer_address) { this.customer_address = customer_address; }

	public CustomerContactDetails getCustomerContactDetails() { return customer_contact_details; }
	public void setCustomerContactDetails(CustomerContactDetails customer_contact_details) { this.customer_contact_details = customer_contact_details; }

	public CustomerOccupation getCustomerOccupationDetails() { return customer_occupation; }
	public void setCustomerOccupationDetails(CustomerOccupation customer_occupation) { this.customer_occupation = customer_occupation; }

	public CustomerMarrigeImage getCustomerMarriageImage() { return customer_married_img; }
	public void setCustomerMarriageImage(CustomerMarrigeImage customer_married_img) { this.customer_married_img = customer_married_img; }

	public CustomerHeir getCustomerHeir() { return customer_heir; }
}