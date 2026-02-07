package com.example.demo.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ShippingDto;
import com.example.demo.entity.Address;
import com.example.demo.entity.Shipping;
import com.example.demo.repository.ShippingRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShippingService {
	private ShippingRepository shippingRepository;
	public ShippingService(ShippingRepository shippingRepository) {
		super();
		this.shippingRepository= shippingRepository;
	}

	public ApiResponse getShippingStatus(Long orderId) {
		try {
			Shipping shipping=shippingRepository.findShippingByOrderId(orderId);
			if(shipping!=null) {
				return new ApiResponse(true,"Status Successfully Fetched",shipping,HttpStatus.OK.value(),Collections.emptyList());
			}
			return new ApiResponse(false,"Invalid Order Id ",null,HttpStatus.BAD_REQUEST.value(),Collections.emptyList());
			
		} catch (Exception e) {
			return new ApiResponse(false,"Error While Fetching Status of Order",null,HttpStatus.BAD_REQUEST.value(),Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse markShipping(Long shippingId) {
		try {
			Optional<Shipping> shippingOptional = shippingRepository.findById(shippingId);
			if(shippingOptional.isEmpty()) {
				return new ApiResponse(false,"Invalid Shipping Id ",null,HttpStatus.NOT_FOUND.value(),Collections.emptyList());
			}
			Shipping shipping= shippingOptional.get();
			shipping.setShippingStatus("Shipped");
			Shipping savedShipping= shippingRepository.save(shipping);
			return new ApiResponse(true," Marked Shipped Successfully",savedShipping,HttpStatus.OK.value(),Collections.emptyList() );
		} catch (Exception e) {
			return new ApiResponse(false,"Error While Setting Status",null,HttpStatus.BAD_REQUEST.value(),Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse saveShipping(ShippingDto shippingDto) {
		Shipping shipping=new Shipping(null, shippingDto.getOrderId(), shippingDto.getCustomerId(), "Shipping Initiated");
		Shipping savedShipping=shippingRepository.save(shipping);
		
		return new ApiResponse(true,"Shipping object saved",savedShipping,HttpStatus.OK.value(),null);
	}

	public Boolean validateAddress(Address address) {
		if(address.getCity()!=null && address.getPinCode()!=null && address.getState()!=null && address.getStreet()!=null) {
			if(address.getPinCode().length()==6) {
				log.info("Address Validated Addres={}",address);
				return true;
			}
		}
		return false;
	}
	
	
}
