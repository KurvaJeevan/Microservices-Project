//package com.gc.service;
//
//import static org.mockito.ArgumentMatchers.anyString;
//
//import java.util.concurrent.ScheduledFuture;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
//@ExtendWith(MockitoExtension.class)
//class DynamicSchedulerServiceTest {
//
//	@Mock
//	private MailService mailService;
//	@Mock
//	private ScheduledFuture<?> scheduledFuture;
//	@Mock
//	private ThreadPoolTaskScheduler taskScheduler;
//
//	@InjectMocks
//	private DynamicSchedulerService dynamicSchedulerService;
//
//	@Test
//	void testSendMailWithScheduler() {
//		dynamicSchedulerService.sendMailWithScheduler(anyString() + "@gmail.com", anyString(), anyString());
//		
//	}
//
//}
