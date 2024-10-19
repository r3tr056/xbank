package com.xfinance.xbank.controllers.public_banking.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.xfinance.xbank.models.ERole;
import com.xfinance.xbank.models.Role;
import com.xfinance.xbank.models.User;
import com.xfinance.infrastructure.jwt.payload.request.LoginRequest;
import com.xfinance.infrastructure.jwt.payload.request.SignupRequest;
import com.xfinance.infrastructure.jwt.payload.response.JwtResponse;
import com.xfinance.infrastructure.jwt.payload.response.MessageResponse;
import com.xfinance.infrastructure.jwt.repo.RoleRepo;
import com.xfinance.infrastructure.jwt.repo.UserRepo;
import com.xfinance.infrastructure.jwt.security.jwt.JwtUtils;
import com.xfinance.infrastructure.jwt.security.services.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@AutoWired
	private AuthenticationManager authManager;

	// TODO : replace user repo and role repos with services
	@AutoWired
	private UserRepo userRepo;

	@AutoWired
	private RoleRepo roleRepo;

	@AutoWired
	private PasswordEncoder passEncoder;

	@AutoWired
	private JwtUtils jwtUils;

	@AutoWired
	private OtpService otpService;

	/**
	 * REST Authenticate Method for `/signin` endpoint
	 * 
	 * @param loginRequest : Login Request
	 * @return ResponseEntity : Response data
	 */
	@PostMapping(value="/signin", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication auth = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(auth);
			String jwt = jwtUtils.generateJwtToken(auth);

			UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

		} catch (AuthenticationException ex) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
	}

	/**
	 * REST User Signup Methods for `/signup` endpoint
	 * 
	 * @param singUpRequest : Sign Up Request
	 * @return ResponseEntity
	 */
	@PostMapping(value="/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepo.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"))
		}

		if (userRepo.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepo.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error : Role is not found."))
						roles.add(adminRole);
						break;

					case "mod":
						Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
						roles.add(modRole);
						break;

					default:
						Role userRole = roleRepo.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepo.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	/**
	 * REST OTP Verify Method for `/otp/verify` endpoint
	 *
	 * @param verifyTokenRequest : Verify Token Request Body
	 * @return ResponseEntity
	 */
	@PostMapping(value="otp/verify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyTokenRequestDTO verifyTokenRequest) {
		String username = verifyTokenRequest.getUsername();
		String email = verifyTokenRequest.getEmail();
		Integer otp = verifyTokenRequest.getOtp();
		Boolean rememberMe = verifyTokenRequest.getRememberMe();

		boolean isOtpValid = otpService.validateOtp(username, otp);
		if (!isOtpValid) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtils.generateJwtTokenAfterVerifiedOtp(username, rememberMe);
		return ResponseEntity.ok(new JwtResponse(token, usenrname, email));
	}
}