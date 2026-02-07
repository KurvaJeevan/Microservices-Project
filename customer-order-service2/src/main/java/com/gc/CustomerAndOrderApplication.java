package com.gc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CustomerAndOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerAndOrderApplication.class, args);
	}

}
