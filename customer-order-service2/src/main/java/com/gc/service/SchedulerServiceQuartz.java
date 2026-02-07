package com.gc.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gc.dto.ApiResponse;
import com.gc.payload.EmailRequestPayload;
import com.gc.quartzscheduleservice.OrderStatusJob;
import com.gc.quartzscheduleservice.ScheduleJob;
import com.gc.quartzscheduleservice.ShippingJob;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SchedulerServiceQuartz {
	private Scheduler scheduler;

	public SchedulerServiceQuartz(Scheduler scheduler) {
		super();
		this.scheduler = scheduler;
	}

	public ApiResponse scheduleEmail(EmailRequestPayload emailRequest) {
		try {
			log.info("Scheduling Email using Quartz Initiated");
			ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getZoneId());
			if (dateTime.isBefore(ZonedDateTime.now())) {
				log.warn("Date and Time must be after Current Time");
				return new ApiResponse(false, "Date and Time must be after Current Time", null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			JobDetail jobDetail = buildJobDetail(emailRequest);
			Trigger trigger = buildTrigger(jobDetail, dateTime, "email-triggers", "Send Email Trigger");

			scheduler.scheduleJob(jobDetail, trigger);
			log.info("Email Scheduled Successfully with Quartz");
			return new ApiResponse(true, "Email Scheduled Successfully",
					jobDetail.getKey().getName() + " " + jobDetail.getKey().getGroup(), null, Collections.emptyList());
		} catch (Exception e) {
			log.error("Error While Scheduling Email {}", e);
			return new ApiResponse(false, "Error While Scheduling Email.", null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.emptyList());
		}
	}

	private JobDetail buildJobDetail(EmailRequestPayload emailRequest) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("email", emailRequest.getEmail());
		jobDataMap.put("body", emailRequest.getBody());
		jobDataMap.put("subject", emailRequest.getSubject());

		return JobBuilder.newJob(ScheduleJob.class).withIdentity(UUID.randomUUID().toString(), "email-jobs")
				.withDescription("Sending Email Job").usingJobData(jobDataMap).storeDurably().build();
	}

	private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt, String triggerName, String description) {
		return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName(), triggerName)
				.withDescription(description).startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
	}

	public ApiResponse scheduleOrderStatus(Long orderid, String status, Long customerId, LocalDateTime date) {
		try {
			log.info("Scheduling Order Status using Quartz Initiated");
			ZonedDateTime dateTime = ZonedDateTime.of(date, ZoneId.of("Asia/Kolkata"));
			if (dateTime.isBefore(ZonedDateTime.now())) {
				return new ApiResponse(false, "Date Time must be after current time", null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("orderId", orderid);
			jobDataMap.put("status", status);
			jobDataMap.put("customerId", customerId);

			JobDetail jobDetail = JobBuilder.newJob(OrderStatusJob.class)
					.withIdentity(UUID.randomUUID().toString(), "ChangeOrderStatus-job")
					.withDescription("Changing the Job status to " + status).usingJobData(jobDataMap).storeDurably()
					.build();
			Trigger trigger = buildTrigger(jobDetail, dateTime, "order-status-change", "Change Order Status");

			scheduler.scheduleJob(jobDetail, trigger);
			log.info("Order Status Change Triggered");
			return new ApiResponse(true, "Order Status Change Triggered",
					jobDetail.getKey().getName() + " " + jobDetail.getKey().getGroup(), null, Collections.emptyList());
		} catch (Exception e) {
			log.error("Error While Changing the Order Status {}", e);
			return new ApiResponse(false, "Error While Changing the order Status.", null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.emptyList());
		}
	}

	public ApiResponse scheduleShippingService(Long orderid, Long customerId, LocalDateTime date) {
		try {
			log.info("Scheduling Schipping Service using Quartz Initiated");
			ZonedDateTime dateTime = ZonedDateTime.of(date, ZoneId.of("Asia/Kolkata"));
			if (dateTime.isBefore(ZonedDateTime.now())) {
				return new ApiResponse(false, "Date Time must be after current time", null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("orderId", orderid);
			jobDataMap.put("customerId", customerId);
			JobDetail jobDetail = JobBuilder.newJob(ShippingJob.class)
					.withIdentity(UUID.randomUUID().toString(), "ShippingService-job")
					.withDescription("Changing Shipping status").usingJobData(jobDataMap).storeDurably().build();
			Trigger trigger = buildTrigger(jobDetail, dateTime, "shipping-triggers", "Send Shipping Trigger");

			scheduler.scheduleJob(jobDetail, trigger);
			log.info("Shipping status change Triggered");
			return new ApiResponse(true, "Shipping status change Triggered",
					jobDetail.getKey().getName() + " " + jobDetail.getKey().getGroup(), null, Collections.emptyList());
		} catch (Exception e) {
			log.error("Error While Changing the Shipping Status {}", e);
			return new ApiResponse(false, "Error While changing shipping status.", null,
					HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.emptyList());
		}
	}

}
