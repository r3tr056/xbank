package com.xfinance.xbank.models.public_banking.third_party_apps;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationData {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	private String store_data_id;

	private String app_type;
	private StoreData store_data;
	private LocalDateTime created;

	public boolean tag_all_invoices;
	public string app_settings;

	public ApplicationData(String name, String store_data_id, String app_type, StoreData store_data, boolean tag_all_invoices) {
		this.name = name;
		this.store_data_id = store_data_id;
		this.app_type = app_type;
		this.store_data = store_data;
		this.tag_all_invoices = tag_all_invoices;
		this.app_settings = app_settings;
	}

}