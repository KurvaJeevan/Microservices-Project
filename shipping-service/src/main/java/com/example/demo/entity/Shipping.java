package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shippingId;
	private Long orderId;
	private Long customerId;
	private String shippingStatus="Shipping Initiated";
}
