package com.xfinance.xbank.public_banking.emailservice;

public interface EmailService {
	public void sendUserRegisterMail(String reciver, String identifier);

	public Boolean sendSimpleMessage(Email email);

	public Boolean sendMessageWithCC(Email email);

    public Boolean sendMessageWithAttachment(Email email) throws IOException, MessagingException;
}