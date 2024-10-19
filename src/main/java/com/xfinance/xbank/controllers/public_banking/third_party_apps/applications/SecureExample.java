package com.xfinance.xbank.controllers.public_banking.third_party_apps.applications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {
	private static final Logger LOG = LoggerFactory.getLogger(SecuredController.class);

	@GetMapping(value="/api/v1/secure")
	public ResponseEntity secure() {
		LOG.info("Received Request : /api/v1/secure");
		return ResponseEntity.ok().build();
	}
}