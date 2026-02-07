package com.gc.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.gc.payload.ShippingPayload;

class ShippingDtoTest {

	@Test
	void test() {
		ShippingPayload dto= new ShippingPayload();
		dto.setCustomerId(1L);
		dto.setOrderId(2L);
		dto.setShippingId(5L);
		dto.setShippingStatus("Shipped");
		assertEquals(1L, dto.getCustomerId());
		assertEquals(2L, dto.getOrderId());
		assertEquals(5L, dto.getShippingId());
		assertEquals("Shipped", dto.getShippingStatus());
	}

}
