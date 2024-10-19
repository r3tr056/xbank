
package com.xfinance.xbank.services.public_banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PublicBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicBankingApplication.class, args);
	}
}