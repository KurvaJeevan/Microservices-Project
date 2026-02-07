package com.example.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ShippingDto;
import com.example.demo.entity.Address;
import com.example.demo.service.ShippingService;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

	private ShippingService shippingService;

	public ShippingController(ShippingService shippingService) {
		super();
		this.shippingService = shippingService;
	}

	@GetMapping("/status/{orderId}")
	public ApiResponse getStatusOfOrder(@PathVariable Long orderId) {
		return shippingService.getShippingStatus(orderId);
	}

	@PostMapping("/create")
	public ApiResponse saveShipping(@RequestBody ShippingDto shippingDto) {
		return shippingService.saveShipping(shippingDto);
	}

	@GetMapping("/mark-shipped/{shippingId}")
	public ApiResponse markShipped(@PathVariable Long shippingId) {
		return shippingService.markShipping(shippingId);
	}

	@PostMapping("/validate")
	public Boolean validateShippingAddress(@RequestBody Address address) {
		return shippingService.validateAddress(address);
	}
}
