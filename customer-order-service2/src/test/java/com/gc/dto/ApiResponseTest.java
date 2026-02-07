package com.gc.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiResponseTest {

	@Test
	void test() {
		ApiResponse apiResponse= new ApiResponse();
		apiResponse.setData("Data");
		apiResponse.setErrors(Collections.emptyList());
		apiResponse.setMessage("Message");
		apiResponse.setStatusCode(HttpStatus.OK.value());
		apiResponse.setSuccess(true);
		assertEquals("Data", apiResponse.getData());
		assertEquals("Message", apiResponse.getMessage());
		assertEquals(HttpStatus.OK.value(), apiResponse.getStatusCode());
		assertEquals(Collections.emptyList(), apiResponse.getErrors());
		assertEquals(true, apiResponse.isSuccess());
		
	}

}
