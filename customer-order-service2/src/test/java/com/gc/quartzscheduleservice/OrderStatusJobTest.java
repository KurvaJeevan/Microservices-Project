package com.gc.quartzscheduleservice;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpStatus;

import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.entity.Product;
import com.gc.repository.CustomerRepository;
import com.gc.service.MailService;
import com.gc.service.OrderService;
import com.gc.utils.MailTemplateUtill;

@ExtendWith(MockitoExtension.class)
class OrderStatusJobTest {
	@Mock
	private MailTemplateUtill mailTemplateUtill;
	@Mock
	private OrderService orderService;
	@Mock
	private MailService mailService;
	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private OrderStatusJob orderStatusJob;
	@Mock
	private JobExecutionContext context;
	@Mock
	private Product product;
	@Mock
	private Address address;

	@Test
	@DisplayName("Success OrderStatus Job")
	void testExecuteInternal() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("status", "Placed");
		jobDataMap.put("customerId", 10L);
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderService.getOrderById(anyLong()))
				.thenReturn(new ApiResponse(true, null, order, HttpStatus.OK.value(), null));
		when(mailTemplateUtill.buildOrderStatusChangedMail(customer, order, "Shipped")).thenReturn(new ApiResponse(true,"Dummy","Dummy",HttpStatus.OK.value(),null));
		orderStatusJob.executeInternal(context);
	}

	@Test
 	@DisplayName("Success OrderStatus Job")
	void testExecuteInternalException() throws JobExecutionException {
		orderStatusJob.executeInternal(context);
	}
}
