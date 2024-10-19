package com.xfinance.xbank.models.public_banking.third_party_apps;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class APIKeyData {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String store_id;

	private String user_id;

	private APIKeyType key_type;

	private StoreData store_data;
	private ApplicationUser app_user;
	public String label;

	private Date created_at;
	private String created_by;

	public APIKeyData(String store_id, String user_id, APIKeyType key_type, StoreData store_data, ApplicationUser app_user, String label, String created_by) {
		
		this.store_id = store_id;
		this.user_id = user_id;
		this.key_type = key_type;
		this.store_data = store_data;
		this.app_user = app_user;
		this.label = label;
		this.created_by = created_by;
	}

}