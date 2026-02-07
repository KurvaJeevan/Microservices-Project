package com.gc.quartzscheduleservice;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gc.service.MailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleJob extends QuartzJobBean {

	private MailService mailService;

	public ScheduleJob(MailService mailService) {
		super();
		this.mailService = mailService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("Schedule Job Execution Initiated");
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			mailService.sendMail(jobDataMap.getString("email"), jobDataMap.getString("subject"),
					jobDataMap.getString("body"));
			log.info("Scheduled Job Execution Completed");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
