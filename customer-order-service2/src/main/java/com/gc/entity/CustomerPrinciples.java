package com.gc.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomerPrinciples implements UserDetails{
	
	private Customer customer;
	
	public CustomerPrinciples(Customer customer) {
		this.customer=customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getUsername();
	}

	public Customer getCustomer() {
		return this.customer;
	}

}
