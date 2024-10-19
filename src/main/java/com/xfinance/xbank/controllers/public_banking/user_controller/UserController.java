package com.xfinance.xbank.controllers.public_banking.user_controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import com.xfinance.xbank.controllers.public_banking.user_controller.UserService;

@RestController;
@RequestMapping("/api/users")
public class UserController {
	// Controller Logger
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@AutoWired
	private final UserService userService;

	@AutoWired
	public UserController(UserService userService) { this.userService = userService; }

	@PostMapping
	public UserOut create(@Valid @RequestBody UserCreateRequest userCreate) {
		return userService.craete(userCreate);
	}

	@PostMapping("/register/user")
	@Secured("ROLE_USER")
	public UserOut registerUser(@RequestBody @Valid UserRegisterRequest userRegister) {
		return userService.registerUser(userRegister);
	}

	@GetMapping("/type/{type}")
	public List<UserOut> findByUserType(@PathVariable("type") UserRole.UserType userType, @RequestParam("disableOnly") Optional<String> disabledOnly) {
		if (disabledOnly.isPresent()) {
			return userService.findAllByUserTypeAndNotEnabled(userType);
		} else {
			return userService.findAllByUserType(userType);
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public UserOut findById(@PathVariable("id") Long id) {
		return userService.findByID(id);
	}

	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public UserOut findByIdentifier(@PathVariable("identifier") String identifier) {
		return userService.findByIdentifier(identifier);
	}

	@PatchMapping("/{id}/stauts")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public UserOut changeLockStatus(@PathVariable("id") Long id) {
		return userService.changeStatus(id);
	}

	@PatchMapping("/{id}/activate")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public UserOut changeEnableStatus(@PathVariable("id") Long id) {
		return userService.changeEnableStatus("id");
	}

	@GetMapping("/auth/current")
	@PreAuthorize("isAuthenticated()")
	public UserOut findCurrentUser() {
		return userService.findCurrentUser();
	}

	@PatchMapping("/password/edit")
	public UserOut updatePassword(@Valid @RequestBody PasswordEditRequest passwordEdit) {
		return userService.updatePassword(passwordEdit);
	}
}