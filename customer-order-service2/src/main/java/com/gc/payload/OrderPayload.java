package com.gc.payload;



import com.gc.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderPayload {
	private Long productId;
	private Integer orderQuantity;
	private Address address;
}
