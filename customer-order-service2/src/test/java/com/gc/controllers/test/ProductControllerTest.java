package com.gc.controllers.test;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.controllers.ProductController;
import com.gc.dto.ApiResponse;
import com.gc.entity.Product;
import com.gc.payload.ProductPayload;
import com.gc.service.ProductService;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;
	@Mock
	private Product product;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Getting Product by id from Controller")
	void testGetProductById() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Product Found", new Product(), HttpStatus.OK.value(), null);
		when(productService.getProductById(Mockito.anyLong())).thenReturn(rs);
		mockMvc.perform(get("/api/products/{id}", Mockito.anyLong())).andDo(print()).andExpect(status().is(200));

	}

	@Test
	@DisplayName("Getting All Products from Controller")
	void testGetAllProducts() throws Exception {
		ApiResponse rs = new ApiResponse(true, "All Products", null, HttpStatus.OK.value(),
				Collections.emptyList());
		when(productService.getAllProducts()).thenReturn(rs);
		mockMvc.perform(get("/api/products")).andDo(print()).andExpect(status().is(200));
	}

	@Test
	void testSaveProduct() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Product Saved Successfully", product,
				HttpStatus.OK.value(), Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(product);
		when(productService.saveProduct(any(ProductPayload.class))).thenReturn(rs);
		mockMvc.perform(post("/api/products/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
