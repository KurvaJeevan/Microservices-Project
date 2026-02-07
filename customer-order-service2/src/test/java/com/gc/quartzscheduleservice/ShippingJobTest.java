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
import com.gc.feignclient.ShippingClient;
import com.gc.payload.ShippingPayload;
import com.gc.repository.CustomerRepository;
import com.gc.repository.OrderRepository;
import com.gc.service.MailService;

@ExtendWith(MockitoExtension.class)
class ShippingJobTest {
	@Mock
	private ShippingClient shippingClient;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private MailService mailService;
	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private ShippingJob shippingJob;
	@Mock
	private JobExecutionContext context;
	@Mock
	private Product product;
	@Mock
	private Address address;
	
	
	@Test
	@DisplayName("Successfully Executed ExecuteInternal Shipping Job")
	void testShippingJobExecuteInternal() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		ShippingPayload shippingDto= new ShippingPayload(5L, 5L, 1L, "Shipping Initiated");
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(5L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true, "Success", shippingDto, HttpStatus.OK.value(), null));
		shippingJob.executeInternal(context);
	}
	
	@Test
	void testShippingJobExecuteInternalException() throws JobExecutionException {
		shippingJob.executeInternal(context);
	}
	
	@Test
	void testShippingJobExecuteInternalCustomerException() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		shippingJob.executeInternal(context);
	}
	@Test
	void testShippingJobExecuteInternalOrderException() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		shippingJob.executeInternal(context);
	}
	
	@Test
	void testShippingJobExecuteInternalShippingStatusException() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		ShippingPayload shippingDto= new ShippingPayload(5L, 5L, 1L, "Shipping");
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(5L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true, "Success", shippingDto, HttpStatus.OK.value(), null));
		shippingJob.executeInternal(context);
	}
	@Test
	void testShippingJobExecuteInternalShippingIdException() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		ShippingPayload shippingDto= new ShippingPayload(15L, 6L, 1L, "Shipping Initiated");
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(5L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 100L,customer.getUsername());
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true, "Success", shippingDto, HttpStatus.OK.value(), null));
		shippingJob.executeInternal(context);
	}
	@Test
	void testShippingJobExecuteInternalShippingStatusEception() throws JobExecutionException {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("orderId", 5L);
		jobDataMap.put("customerId", 10L);
		ShippingPayload shippingDto= new ShippingPayload(15L, 6L, 1L, "Shipping Initiated");
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(5L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 100L,customer.getUsername());
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(false, "Failed", shippingDto, HttpStatus.OK.value(), null));
		shippingJob.executeInternal(context);
	}

}
