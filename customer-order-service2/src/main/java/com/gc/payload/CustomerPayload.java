package com.gc.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerPayload {
	private String name;
	private String email;
	private String username;
	private Long mobileNo;
	private String password;
}
