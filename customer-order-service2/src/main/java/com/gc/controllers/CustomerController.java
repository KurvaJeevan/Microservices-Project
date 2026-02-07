package com.gc.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gc.dto.ApiResponse;
import com.gc.payload.CustomerLoginPayload;
import com.gc.payload.CustomerPayload;
import com.gc.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {
	
	
	private CustomerService customerService;
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@PostMapping("/register")
	public ApiResponse registerCustomer(@RequestBody CustomerPayload customerPayload) {
		log.trace("registerCustomer called in CustomerController");
		return customerService.registerCustomer(customerPayload);
	}
	
	@PostMapping("/login")
	public ApiResponse customerLogin(@RequestBody CustomerLoginPayload customerLoginPayload) {
		log.trace("customerLogin Called in CustomerController");
		return customerService.loginCustomer(customerLoginPayload);
	}
	
	@GetMapping("/allorderlist/{customerId}")
	public ApiResponse getCustomerOrderList(@PathVariable Long customerId) {
		log.trace("getCustomerOrderList Called in CustomerController");
		return customerService.getCustomerOrderList(customerId);
	}
	
	@PutMapping("/{customerId}")
	public ApiResponse updateCustomerDetails(@RequestBody CustomerPayload customerPayload,@PathVariable Long customerId) {
		log.trace("updateCustomerDetails Called in CustomerController");
		return customerService.updateCustomerDetails(customerPayload,customerId);
	}
	
	@DeleteMapping("/{customerId}")
	public ApiResponse deleteCustomer(@PathVariable Long customerId) {
		log.trace("deleteCustomer Called in CustomerController");
		return customerService.deleteCustomer(customerId);
	}
	
}
