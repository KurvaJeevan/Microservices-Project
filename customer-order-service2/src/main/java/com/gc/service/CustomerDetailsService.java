package com.gc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gc.entity.Customer;
import com.gc.entity.CustomerPrinciples;
import com.gc.exception.NotFoundException;
import com.gc.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerDetailsService implements UserDetailsService {
	
	private CustomerRepository customerRepository;
	public CustomerDetailsService(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			log.info("LoadUserByUsername Entered");
			Customer c= customerRepository.findCustomerByUsername(username).orElseThrow(()->new NotFoundException("User Not Found"));
			log.info("Customer Fetched Successfully");
			return new CustomerPrinciples(c);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}


}
