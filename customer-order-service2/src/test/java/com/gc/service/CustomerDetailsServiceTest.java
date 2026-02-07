package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.gc.entity.Customer;
import com.gc.entity.CustomerPrinciples;
import com.gc.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private CustomerDetailsService customerDetailsService;
	@Mock
	private Customer customer;

	@Test
	@DisplayName("Loading User from its Username - CustomerDetailsService")
	void testLoadUserByUsername() {
		when(customerRepository.findCustomerByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		UserDetails details = customerDetailsService.loadUserByUsername(customer.getUsername());
		assertEquals(customer, new CustomerPrinciples(customer).getCustomer());
		assertEquals(customer.getUsername(), details.getUsername());
		assertEquals(customer.getPassword(), details.getPassword());
		assertEquals(Collections.emptyList(), details.getAuthorities());
	}

	@Test
	@DisplayName("Exception While Loading User from its Username - CustomerDetailsService")
	void testLoadUserByUsernameException() {
		when(customerRepository.findCustomerByUsername(customer.getUsername())).thenReturn(Optional.empty());
		UserDetails details = customerDetailsService.loadUserByUsername(customer.getUsername());
		assertEquals(null, details);
	}

}
