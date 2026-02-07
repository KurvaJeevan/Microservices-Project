package com.gc.service;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DynamicSchedulerService {

	private MailService mailService;
	private ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	private ScheduledFuture<?> scheduledFuture;

	public DynamicSchedulerService(MailService mailService) {
		super();
		this.mailService = mailService;
		taskScheduler.initialize();
	}

	public void sendMailWithScheduler(String tomail, String subject, String body) {
		log.info("Sending Email with Scheduler Initiated");
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
		Runnable task = () -> {
			try {
				mailService.sendMail(tomail, subject, body);
			} catch (MessagingException e) {
				log.error("Error While Sending Email");
				log.error(e.getMessage());
				return;
			}
		};
		Instant runAt = Instant.now().plusSeconds(10);
		scheduledFuture = taskScheduler.schedule(task, triggerContext -> runAt);
		log.info("Schedule Assigned");
	}
}
