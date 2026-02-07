package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.gc.dto.ApiResponse;
import com.gc.payload.EmailRequestPayload;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceQuartzTest {
	@Mock
	private Scheduler scheduler;
	@InjectMocks
	private SchedulerServiceQuartz serviceQuartz;
	@Mock
	private EmailRequestPayload emailRequest;

	@Test	
	@DisplayName("Success Scheduling Email - SchedulerServiceQuartz")
	void testScheduleEmail() throws SchedulerException {
		EmailRequestPayload emailRequest= new EmailRequestPayload("dummy@gmail.com","DUmmy", "DUmmy", LocalDateTime.now().plusMinutes(5), ZoneId.systemDefault());
		ApiResponse rs= serviceQuartz.scheduleEmail(emailRequest);
		assertEquals(true, rs.isSuccess());
		assertEquals("Email Scheduled Successfully", rs.getMessage());
	}
	
	@Test	
	@DisplayName("Date Exception while Scheduling Email - SchedulerServiceQuartz")
	void testScheduleEmailDateTimeBefore() throws SchedulerException {
		EmailRequestPayload emailRequest= new EmailRequestPayload("dummy@gmail.com","DUmmy", "DUmmy", LocalDateTime.now().minusMinutes(1), ZoneId.systemDefault());
		ApiResponse rs= serviceQuartz.scheduleEmail(emailRequest);
		assertEquals(false, rs.isSuccess());
		assertEquals("Date and Time must be after Current Time", rs.getMessage());
	}
	@Test
	@DisplayName("Exception while Scheduling Email - SchedulerServiceQuartz")
	void testScheduleEmailException() {
		ApiResponse rs= serviceQuartz.scheduleEmail(emailRequest);
		assertEquals(false, rs.isSuccess());
		assertEquals("Error While Scheduling Email.", rs.getMessage());
	}
	
	@Test
	@DisplayName("Success While Scheduling Order Status - SchedulerServiceQuartz")
	void testScheduleOrderStatus() {
		ApiResponse rs= serviceQuartz.scheduleOrderStatus(5L,"Placed",2L,LocalDateTime.now().plusMinutes(5));
		assertEquals(true, rs.isSuccess());
		assertEquals("Order Status Change Triggered", rs.getMessage());
	}
	@Test
	@DisplayName("Date Exception While Scheduling Order Status - SchedulerServiceQuartz")
	void testScheduleOrderStatusDateAndTimeExpired() {
		ApiResponse rs= serviceQuartz.scheduleOrderStatus(5L,"Placed",2L,LocalDateTime.now().minusMinutes(5));
		assertEquals(false, rs.isSuccess());
		assertEquals("Date Time must be after current time", rs.getMessage());
	}
	@Test
	@DisplayName("Exception While Scheduling Order Status - SchedulerServiceQuartz")
	void testScheduleOrderStatusException() {
		ApiResponse rs= serviceQuartz.scheduleOrderStatus(1L,"Placed",2L,null);
		assertEquals(false, rs.isSuccess());
		assertEquals("Error While Changing the order Status.", rs.getMessage());
	}
	
	@Test
	@DisplayName("Success While Scheduling Order Status - SchedulerServiceQuartz")
	void testscheduleShippingService() {
		ApiResponse rs= serviceQuartz.scheduleShippingService(5L,2L,LocalDateTime.now().plusMinutes(5));
		assertEquals(true, rs.isSuccess());
		assertEquals("Shipping status change Triggered", rs.getMessage());
	}
	@Test
	@DisplayName("Date Exception While Scheduling Order Status - SchedulerServiceQuartz")
	void testscheduleShippingServiceDateAndTimeExpired() {
		ApiResponse rs= serviceQuartz.scheduleShippingService(5L,2L,LocalDateTime.now().minusMinutes(5));
		assertEquals(false, rs.isSuccess());
		assertEquals("Date Time must be after current time", rs.getMessage());
	}
	@Test
	@DisplayName("Exception While Scheduling Order Status - SchedulerServiceQuartz")
	void testscheduleShippingServiceException() {
		ApiResponse rs= serviceQuartz.scheduleShippingService(1L,2L,null);
		assertEquals(false, rs.isSuccess());
		assertEquals("Error While changing shipping status.", rs.getMessage());
	}
	
}
