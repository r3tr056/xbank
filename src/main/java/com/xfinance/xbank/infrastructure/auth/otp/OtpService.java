package com.xfinance.xbank.infrastructure.auth.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xfinance.xbank.infrastructure.auth.dto.EmailDTO;

@Description(value="Service responsible for handling OTP related functionality.")
@Service
public class OtpService {
	
	private final Logger logger = LoggerFactory.getLogger(OtpService.class);

	private OtpGenerator otpGen;
	private EmailService emailService;
	private UserService userService;

	public OtpService(OtpGenerator otpGen, EmailService emailService, UserService userService) {
		this.otpGen = otpGen;
		this.emailService = emailService;
		this.userService = userService;
	}

	public Boolean generateOtp(String key) {
		// generate OTP
		Integer otpValue = otpGen.generateOtp(key);
		if (otpValue == -1) {
			logger.error("OTP generator is not working...");
			throw new RuntimeException("OTP generator is not working, Restart the `auth.otp.OtpService`");
		}

		String userEmail = userService.findEmailByUsername(key);
		List<String> recipients = new ArrayList<>();
		recipients.add(userEmail);

		// generate emailDTO object
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.useTemplate("publicbanking.auth.EmailOTPTemplate")
		emailDTO.setOTPValue(otpValue);

		return emailService.sendSimpleMessage(emailDTO);
	}

	public Boolean validateOTP(String key, Integer otpNumber) {
		Integer cacheOTP = this.otpGen.getOTPByKey(key);
		if (cacheOTP != null && cacheOTP.equals(otpNumber)) {
			this.otpGen.clearOTPFromCache(key);
			return true;
		}

		return false;
	}
}