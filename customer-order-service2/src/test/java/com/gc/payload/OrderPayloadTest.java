package com.gc.payload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gc.entity.Address;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class OrderPayloadTest {
	@Mock
	private Address address;
	@Test
	@DisplayName("Testing Getters an Setters for OrderPayload")
	void test() {
		OrderPayload dto= new OrderPayload(1L, 5, address);
		OrderPayload dto2= new OrderPayload();
		dto2.setAddress(address);
		dto2.setOrderQuantity(10);
		dto2.setProductId(5L);
		log.info(dto2+"");
		assertEquals(1L, dto.getProductId());
		assertEquals(5, dto.getOrderQuantity());
		assertEquals(address, dto.getAddress());
	}

}
