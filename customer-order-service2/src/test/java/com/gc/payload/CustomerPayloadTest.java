package com.gc.payload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CustomerPayloadTest {

	@Test
	void testGettersAndSetters() {
		CustomerPayload customerPayload= new CustomerPayload();
		customerPayload.setEmail("Jeevan@gmail.com");
		customerPayload.setMobileNo(994957L);
		customerPayload.setName("Jeevan");
		customerPayload.setPassword("Jeevan13");
		customerPayload.setUsername("Jeevan12");
		assertEquals("Jeevan@gmail.com",customerPayload.getEmail() );
		assertEquals(994957L,customerPayload.getMobileNo() );
		assertEquals("Jeevan",customerPayload.getName() );
		assertEquals("Jeevan13",customerPayload.getPassword() );
		assertEquals("Jeevan12",customerPayload.getUsername() );
	}

}
