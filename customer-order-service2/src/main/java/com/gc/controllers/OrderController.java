package com.gc.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gc.dto.ApiResponse;
import com.gc.payload.DeleteOrderPayload;
import com.gc.payload.OrderPayload;
import com.gc.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/create/{customerId}")
	public ApiResponse saveOrder(@RequestBody OrderPayload orderPayload,@PathVariable Long customerId) {
		log.trace("saveOrder called in OrderController");
		return orderService.saveOrder(orderPayload,customerId);
	}

	@GetMapping("/{id}")
	public ApiResponse getOrderById(@PathVariable Long id) {
		log.trace("getOrderById called in OrderController");
		return orderService.getOrderById(id);
	}

	@GetMapping
	public ApiResponse getAllOrders() {
		log.trace("getAllOrders called in OrderController");
		return orderService.getAllOrders();
	}

	@PatchMapping("/changeShippingStatus/{orderId}/{customerId}")
	public ApiResponse changeShippingStatus(@PathVariable Long orderId,@PathVariable Long customerId) {
		log.trace("changeShippingStatus called in OrderController");
		return orderService.changeShippingStatus(orderId,customerId);
	}
	
	@DeleteMapping
	public ApiResponse deleteOrder(@RequestBody DeleteOrderPayload deleteOrderPayload) {
		log.trace("deleteOrder called in OrderController");
		return orderService.deleteOrder(deleteOrderPayload);
	}
	
	@PutMapping("/{orderId}")
	public ApiResponse updateOrder(@RequestBody OrderPayload orderPayload,@PathVariable Long orderId) {
		log.trace("updateOrder called in OrderController");
		return orderService.updateOrder(orderPayload,orderId);
	}

}
