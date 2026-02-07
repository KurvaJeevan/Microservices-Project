package com.gc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.entity.Product;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorTest {

	@Mock
	private Order order;
	@Mock
	private Customer customer;

	@Test
	@DisplayName("Exception While Generating OrderReceipt - PdfGenerator")
	void testGenerateOrderReceiptException() {
		ApiResponse output = PdfGenerator.generateOrderReceipt(order, customer);
		assertEquals(false, output.isSuccess());
	}

	@Test
	@DisplayName("Success while Generating OrderReceiptPdf - PdfGenerator")
	void testGenerateOrderReceipt() {
		Product product = new Product(1L, "Trimmer", 6456.5,LocalDateTime.now());
		Address address = new Address("Banjara Hills", "Hyderabad", "Telangana", "500034");
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, null,"Jeevan");
		ApiResponse output = PdfGenerator.generateOrderReceipt(order, customer);
		assertEquals(true, output.isSuccess());
	}
}
