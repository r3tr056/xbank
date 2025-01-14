package com.xfinance.xbank.infrastructure.auth.payload;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.Set;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class SingupRequest {

	@NotBlank
	@Size(min=3, max=20)
	private String username;

	@NotBlank
	@Size(max=50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min=8, max=40)
	private string password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}
}