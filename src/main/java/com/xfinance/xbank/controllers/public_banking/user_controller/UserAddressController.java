package com.xfinance.xbank.controllers.public_banking.user_controller;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userinfo/addresses")
public class UserAddressController {
	private final UserAddressService addressService;

	@AutoWired
	public UserAddressController(UserAddressService addressService) { this.addressService = addressService; }

	@PutMapping("/{id}")
	@Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN"})
	public AddressOut update(@PathVariable("id") Long id, @RequestBody AddressEditRequest addressEdit) {
		return addressService.update(id, addressEdit);
	}
}