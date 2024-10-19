package com.xfinance.xbank.public_banking.emailservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory

import java.io.IOException;
import java.util.stream.Collectors;
import javax.mail.MessageException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.context.annotation.Description;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.CLassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Description(value="Service layer that implements handlers for sending, receiving and handling emails")
@Service
public class EmailService {

	public final Logger logger = LoggerFactory.getLogger(EmailService.class);

	public final JavaMailSender emailSender;

	@AutoWired
	public EmailService(JavaMailSender emailSender) {
		if (emailSender) {
			this.emailSender = emailSender;
		} else {
			JavaMailSender emailSender = new JavaMailSender();
			// Set up email server for internal VPC network
			// This serves as an example 
			emailSender.setHost("smtp.xbank.com");
			emailSender.setPort(587);
			emailSender.setUsername("support@xbank.com");
			emailSender.setPassword("xbanksecurepassword");

			Properties props = emailSender.getJavaMailProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");
			props.put("mail.debug", "true");

			this.emailSender = emailSender;
		}
	}

	@Async
	@RolesAllowed({"ROLE_SYSTEM", "ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public Boolean sendMail(Email email) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
		message.setSubject(email.getSubject());
		message.setText(email.getRenderedBody());
		Boolean isSent = false;
		try {
			emailSender.send(message);
			isSent = true;
		} catch (Exception ex) {
			logger.error("Sending e-mail error : {}", ex.getMessage());
		}
		return isSent;
	}

	@Async
	@RolesAllowed({"ROLE_SYSTEM"})
	public Boolean sendSystemGeneratedMail(Email email) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
		message.setSubject(email.getSubject());
	}


	@Async
	public Boolean sendMailWithCC(Email email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
		message.setCc(email.getCcList().stream().collect(Collectors.joining(",")));
		message.setSubject(email.getSubject());
		message.setText(email.getRenderedBody());
		Boolean isSent = false;
		try {
			emailSender.send(message);
			isSent = true;
		} catch (Exception ex) {
			logger.error("Sending e-mail error : {}", ex.getMessage());
		}

		return isSent;
	}

	@Async
	public Boolean sendMailWithAttachment(Email email) throws IOException, MessageException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

		messageHelper.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
		messageHelper.setSubject(email.getSubject());
		messageHelper.setText(email.getRenderedBody());

		Resource gcpResource = new Resource(email.getLocationURLString());
		messageHelper.addAttachment("attachment", gcpResource.getFile());

		Boolean isSent = false;
		try {
			emailSender.send(message);
			isSent = true;
		} catch (Exception ex) {
			logger.error("Sending e-mail with attachment error : {}", ex.getMessage());
		}

		return isSent;
	}
}