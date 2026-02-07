package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AddressTest {

	@Test
	void testGettersAndSetters() {
		Address address = new Address("Banjara Hills", "Hyderabad", "Telangana", "500034");
		assertEquals("Banjara Hills", address.getStreet());
		assertEquals("Hyderabad", address.getCity());
		assertEquals("Telangana", address.getState());
		assertEquals("500034", address.getPinCode());
		
		Address address2= new Address();
		address2.setCity("Hyderabad");
		address2.setPinCode("500034");
		address2.setState("Telangana");
		address2.setStreet("Banjara Hills");
		log.info(address2.toString());
	}

}
