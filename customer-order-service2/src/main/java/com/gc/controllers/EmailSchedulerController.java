package com.gc.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gc.dto.ApiResponse;
import com.gc.payload.EmailRequestPayload;
import com.gc.service.SchedulerServiceQuartz;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class EmailSchedulerController {

	private SchedulerServiceQuartz emailSchedulerServiceQuartz;

	public EmailSchedulerController(SchedulerServiceQuartz emailSchedulerServiceQuartz) {
		super();
		this.emailSchedulerServiceQuartz = emailSchedulerServiceQuartz;
	}


	@PostMapping("/schedule/email")
	public ApiResponse scheduleEmail(@RequestBody EmailRequestPayload emailRequest) {
		log.trace("scheduleEmail Called in EmailSchedulerController");
		return emailSchedulerServiceQuartz.scheduleEmail(emailRequest);
	}

}
