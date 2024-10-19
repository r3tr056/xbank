package com.xfinance.xbank.controllers.public_banking.auth.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;

	@NotBlank
	private final String jwtToken;
}