package com.gc.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gc.dto.ApiResponse;
import com.gc.entity.Order;
import com.gc.payload.DeleteOrderPayload;
import com.gc.payload.OrderPayload;
import com.gc.payload.ProductPayload;
import com.gc.service.OrderService;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private OrderService orderService;
	@Autowired
	private ObjectMapper objectMapper;
	@Mock
	private Order order;
	@Mock
	private OrderPayload orderPayload;
	@Mock
	private DeleteOrderPayload deleteOrderPayload;

	@Test
	@DisplayName("Success Saving Order - Order Controller")
	void testSaveOrder() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Order Saved Successfully", order, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(orderPayload);
		when(orderService.saveOrder(any(OrderPayload.class), anyLong())).thenReturn(rs);
		mockMvc.perform(post("/api/order/create/{customerId}", 5L).contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Order Saved Successfully"))
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
	}

	@Test
	@DisplayName("Success Fetching Order with Order Id - Order Controller")
	void testGetOrderById() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Order Found Successfully", order, HttpStatus.OK.value(),
				Collections.emptyList());
		when(orderService.getOrderById(Mockito.anyLong())).thenReturn(rs);
		mockMvc.perform(get("/api/order/{id}", Mockito.anyLong())).andDo(print()).andExpect(status().is(200))
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Order Found Successfully"))
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
	}

	@Test
	@DisplayName("Success Fetching All Orders- Order Controller")
	void testGetAllOrders() throws Exception {
		ApiResponse rs = new ApiResponse(true, "All Orders", new ArrayList<>(Arrays.asList(order, new Order())),
				HttpStatus.OK.value(), Collections.emptyList());
		when(orderService.getAllOrders()).thenReturn(rs);
		mockMvc.perform(get("/api/order")).andDo(print()).andExpect(status().is(200))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.message").value("All Orders"))
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
	}

	@Test
	@DisplayName("Success Changing ShippingStatus- Order Controller")
	void testChangeShippingStatus() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Shipping Status Changed", order, HttpStatus.OK.value(),
				Collections.emptyList());
		when(orderService.changeShippingStatus(anyLong(), anyLong())).thenReturn(rs);
		mockMvc.perform(patch("/api/order/changeShippingStatus/{orderId}/{customerId}", anyLong(), anyLong()))
				.andDo(print()).andExpect(status().is(200)).andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Shipping Status Changed"))
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
	}
	@Test
	@DisplayName("Updating Order from Controller")
	void testUpdateOrder() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Order Updated Successfully", order, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(orderPayload);
		when(orderService.updateOrder(any(OrderPayload.class), anyLong())).thenReturn(rs);
		mockMvc.perform(
				put("/api/order/{orderId}", 5L).contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
	}

	@Test
	@DisplayName("Deleting Order from Controller")
	void testDeleteOrder() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Order Deleted Successfully", null, HttpStatus.OK.value(),
				Collections.emptyList());
		String jsonRequest = objectMapper.writeValueAsString(deleteOrderPayload);
		when(orderService.deleteOrder(any(DeleteOrderPayload.class))).thenReturn(rs);
		mockMvc.perform(delete("/api/order").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andDo(print()).andExpect(status().is(200))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
	}
}
