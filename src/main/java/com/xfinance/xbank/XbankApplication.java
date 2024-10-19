package com.xfinance.xbank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XbankApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(XbankApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Welcome to Xbank");
		System.exit(0);
	}

}
