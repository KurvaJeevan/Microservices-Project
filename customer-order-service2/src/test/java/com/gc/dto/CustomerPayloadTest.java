package com.gc.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.gc.payload.CustomerPayload;

class CustomerPayloadTest {

	@Test
	void testGettersAndSetters() {
		CustomerPayload customerDTO= new CustomerPayload();
		customerDTO.setEmail("Jeevan@gmail.com");
		customerDTO.setMobileNo(994957L);
		customerDTO.setName("Jeevan");
		customerDTO.setPassword("Jeevan13");
		customerDTO.setUsername("Jeevan12");
		assertEquals("Jeevan@gmail.com",customerDTO.getEmail() );
		assertEquals(994957L,customerDTO.getMobileNo() );
		assertEquals("Jeevan",customerDTO.getName() );
		assertEquals("Jeevan13",customerDTO.getPassword() );
		assertEquals("Jeevan12",customerDTO.getUsername() );
	}

}
