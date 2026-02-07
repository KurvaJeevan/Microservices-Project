package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ProductTest {

	@Test
	void test() {
		Product product= new Product();
		product.setProductId(1L);
		product.setProductName("Mouse");
		product.setProductPrice(600.0);
		LocalDateTime date =LocalDateTime.now();
		product.setCreatedAt(date);
		
		Product product2 = new Product(6L, "Keyboard", 800.0,LocalDateTime.now());
		log.info(product2+"");
		
		assertEquals(1L, product.getProductId());
		assertEquals("Mouse", product.getProductName());
		assertEquals(600.0, product.getProductPrice());
		assertEquals(date, product.getCreatedAt());
	}

}
