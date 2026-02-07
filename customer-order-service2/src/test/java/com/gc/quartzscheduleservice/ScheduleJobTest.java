package com.gc.quartzscheduleservice;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gc.service.MailService;

@ExtendWith(MockitoExtension.class)
class ScheduleJobTest {
	@Mock
	private MailService mailService;
	@InjectMocks
	private ScheduleJob scheduleJob;
	@Mock
	private JobExecutionContext context;
	
	@Test
	@DisplayName("Success of ExecuteInternam of ScheduleJob")
	void testExecuteInternalOfScheduleJob() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("email", "jeevan@gmail.com");
		jobDataMap.put("body", "Demo Body");
		jobDataMap.put("subject", "Demo Subject");
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		scheduleJob.executeInternal(context);
	}
	
	@Test
	@DisplayName("Exception ExecuteInternam of ScheduleJob")
	void testExecuteInternalOfScheduleJobException() throws JobExecutionException {
		scheduleJob.executeInternal(context);
	}

}
