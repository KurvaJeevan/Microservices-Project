package com.gc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.entity.Product;
import com.gc.payload.ShippingPayload;

@ExtendWith(MockitoExtension.class)
class MailTemplateUtillTest {
	@InjectMocks
	private MailTemplateUtill mailTemplateUtill;
	@Mock
	private Product product;
	@Mock
	private Customer customer;
	@Mock
	private Address address;
	@Mock
	private Order order;
	@Mock
	private ShippingPayload shippingDto;
	@Test
	void testBuildOrderConfirmationMail() {
		Customer customer= new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order= new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ApiResponse rs = mailTemplateUtill.buildOrderConfirmationMail(customer, order);
		assertEquals(true, rs.isSuccess());
		assertEquals("Body of Order Confirmation mail", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	@Test
	void testBuildOrderShippedMail() {
		Customer customer= new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order= new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ApiResponse rs = mailTemplateUtill.buildOrderShippedMail(customer, order,shippingDto);
		assertEquals(true, rs.isSuccess());
		assertEquals("Body of Order Shipped mail", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	@Test
	void testBuildOrderStatusChangedMail() {
		Customer customer= new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order= new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ApiResponse rs = mailTemplateUtill.buildOrderStatusChangedMail(customer, order,"Shipped");
		assertEquals(true, rs.isSuccess());
		assertEquals("Body of Order Status Changed mail", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}

}
