package com.gc.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Mock
	private JavaMailSender javaMailSender;
	@Mock
	private MimeMessage mimeMessage;
	@InjectMocks
	private MailService mailService;
	
	@Test
	@DisplayName("Success while Sending email to the User -MailService")
	void testMailSent() throws MessagingException {
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		mailService.sendMail("abcd@gmail.com", "Hello", "dhkdjsnkjnvdskj");
		verify(javaMailSender,times(1)).send(mimeMessage);
	}
	
	@Test
	@DisplayName("Success while Sending email with attachment to the User - MailService")
	void testMailSentWithAttachment() throws MessagingException {
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		byte[] attachment = "dummy".getBytes();

		mailService.sendMailWithAttachment("abcd@gmail.com", "Hello", "dhkdjsnkjnvdskj",attachment,"Demo");
		verify(javaMailSender,times(1)).send(mimeMessage);
	}

}
