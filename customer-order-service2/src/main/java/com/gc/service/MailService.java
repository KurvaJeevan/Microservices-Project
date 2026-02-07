package com.gc.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailService {

	private JavaMailSender javaMailSender;

	public MailService(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}

	@Async("asynctaskexecutor")
	public void sendMail(String tomail, String subject, String body) throws MessagingException {
		log.info("Sending Mail Initiated");
		MimeMessage mm = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		mmh.setFrom("kurvajeevankumar@gmail.com");
		mmh.setTo(tomail);
		mmh.setText(body, true);
		mmh.setSubject(subject);
		log.info("Mail Send Successully");
		javaMailSender.send(mm);
	}

	@Async("asynctaskexecutor")
	public void sendMailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) throws MessagingException {
		log.info("Sending Mail Initiated");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);

		helper.addAttachment(filename, new ByteArrayDataSource(attachment, "application/pdf"));
		log.info("Mail sent Successfully with attachment");
		javaMailSender.send(message);

	}

}
