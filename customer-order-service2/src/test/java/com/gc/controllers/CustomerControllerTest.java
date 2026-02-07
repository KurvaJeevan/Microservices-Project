package com.gc.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.payload.CustomerLoginPayload;
import com.gc.payload.CustomerPayload;
import com.gc.payload.ProductPayload;
import com.gc.service.CustomerService;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@Autowired
	private ObjectMapper objectMapper;
	@Mock
	private Customer customer;
	@Mock
	private CustomerPayload customerPayload;
	@Mock
	private CustomerLoginPayload customerLoginDTO;

	@Test
	@DisplayName("This test case is related to Product by Id")
	void testGetProductById() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Order List Successfully fetched", customer.getOrderedList(),
				HttpStatus.OK.value(), Collections.emptyList());
		when(customerService.getCustomerOrderList(anyLong())).thenReturn(rs);
		mockMvc.perform(get("/api/customer/allorderlist/{customerId}",anyLong())).andDo(print()).andExpect(status().is(200));
	}

	@Test
	@DisplayName("Registering a Customer")
	void testRegisterCustomer() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Customer Saved Successfully", customer, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(customer);
		when(customerService.registerCustomer(customerPayload)).thenReturn(rs);
		mockMvc.perform(post("/api/customer/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@DisplayName("This test case is related to logging in a customer")
	void testLoginCustomer() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Login Successfull", customer, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(customer);
		when(customerService.loginCustomer(customerLoginDTO)).thenReturn(rs);
		mockMvc.perform(post("/api/customer/login").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andDo(print()).andExpect(status().isOk());
	}
	@Test
	@DisplayName("Updating Customer from Controller")
	void testUpdateCustomer() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Customer Updated Successfully", customer, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(customerPayload);
		when(customerService.updateCustomerDetails(any(CustomerPayload.class), anyLong())).thenReturn(rs);
		mockMvc.perform(
				put("/api/customer/{customerId}", 5L).contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
	}

	@Test
	@DisplayName("Deleting Customer from Controller")
	void testDeleteCustomer() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Customer Deleted Successfully", null, HttpStatus.OK.value(),
				Collections.emptyList());
		when(customerService.deleteCustomer(anyLong())).thenReturn(rs);
		mockMvc.perform(delete("/api/customer/{customerId}", 5L)).andDo(print()).andExpect(status().is(200))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
	}
}
