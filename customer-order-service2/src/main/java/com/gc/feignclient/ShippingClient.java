package com.gc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.payload.ShippingPayload;

@FeignClient(name = "SHIPPINGSERVICE",url  = "http://localhost:8085", path = "/api/shipping")
public interface ShippingClient {

	@PostMapping("/create")
	ApiResponse saveShipping(@RequestBody ShippingPayload shippingDto);

	@PostMapping("/validate")
	Boolean validateShippingAddress(@RequestBody Address address);

	@GetMapping("/mark-shipped/{shippingId}")
	ApiResponse markShipped(@PathVariable Long shippingId);

	@GetMapping("/status/{orderId}")
	ApiResponse getStatusOfOrder(@PathVariable Long orderId);
}
