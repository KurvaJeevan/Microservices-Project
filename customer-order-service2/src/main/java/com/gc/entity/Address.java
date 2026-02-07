package com.gc.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Getter
@Setter
public class Address {
	
	private String street;
	private String city;
	private String state;
	private String pinCode;
	@Override
	public String toString() {
		return "street=" + street + ", city=" + city + ", state=" + state + ", pinCode=" + pinCode;
	}	
}
