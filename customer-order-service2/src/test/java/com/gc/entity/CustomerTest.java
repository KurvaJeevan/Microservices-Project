package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CustomerTest {

	@Test
	void test() {
		Customer customer= new Customer();
		customer.setEmail("jeevan@gmail.com");
		customer.setId(1L);
		customer.setMobileNo(99495545L);
		customer.setUsername("jeevan");
		customer.setOrderedList(null);
		customer.setName("Jeevan");
		assertEquals("jeevan@gmail.com", customer.getEmail());
		assertEquals(1, customer.getId());
		assertEquals(99495545, customer.getMobileNo());
		assertEquals("jeevan", customer.getUsername());
		assertEquals(null, customer.getOrderedList());
		assertEquals("Jeevan", customer.getName());
	}

}
