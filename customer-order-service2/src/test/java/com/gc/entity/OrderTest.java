package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class OrderTest {
	@Mock
	private Address address;
	@Mock
	private Product product;

	@Test
	void test() {
		LocalDateTime dateTime= LocalDateTime.now();
		Order order= new Order();
		order.setAddress(address);
		order.setOrderId(1L);
		order.setOrderQuantity(5);
		order.setOrderedDate(dateTime);
		order.setOrderStatus("Placed");
		order.setProduct(product);
		order.setShippingId(5L);
		assertEquals(address, order.getAddress());
		assertEquals(1L, order.getOrderId());
		assertEquals(5, order.getOrderQuantity());
		assertEquals(dateTime, order.getOrderedDate());
		assertEquals("Placed", order.getOrderStatus());
		assertEquals(product, order.getProduct());
		assertEquals(5L, order.getShippingId());
	}

}
