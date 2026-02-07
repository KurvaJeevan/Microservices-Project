package com.gc.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.exception.NotFoundException;
import com.gc.payload.CustomerLoginPayload;
import com.gc.payload.CustomerPayload;
import com.gc.repository.CustomerRepository;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

	private CustomerRepository customerRepository;
	private MailService mailService;

	public CustomerService(CustomerRepository customerRepository, MailService mailService) {
		this.customerRepository = customerRepository;
		this.mailService = mailService;
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	public ApiResponse registerCustomer(CustomerPayload customerdto) {

		try {
			log.info("Registering Customer Initiated");
			if (customerRepository.existsByUsername(customerdto.getUsername())) {
				log.warn("Username Already Exists");
				return new ApiResponse(false, "Username Already Exists Please Try new Username", null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			if (customerRepository.existsByEmail(customerdto.getEmail())) {
				log.warn("Email Already Exists");
				return new ApiResponse(false, "Customer Already Exists with the Email, Try Login", null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			Customer c = new Customer(null, customerdto.getName(), customerdto.getEmail(), customerdto.getMobileNo(),
					customerdto.getUsername(), encoder.encode(customerdto.getPassword()), null);
			Customer cust = customerRepository.save(c);
			String subject = "Welcome to CustomerAndOrderService, " + cust.getName() + "! 🎉";

			String body = "<div style='font-family: Arial, sans-serif; padding: 20px; line-height: 1.6;'>"
					+ "<h2 style='color: #2E86C1;'>Welcome to CustomerAndOrderService!</h2>" + "<p>Hi <strong>"
					+ cust.getName() + "</strong>,</p>"
					+ "<p>We're excited to have you on board. Your account has been created successfully, "
					+ "and you can now explore all the features we offer.</p>"
					+ "<p>If you have any questions or need help getting started, feel free to reach out.</p>" + "<br>"
					+ "<p>Thank you for joining us!<br>" + "<strong>The CustomerAndOrderService Team</strong></p>"
					+ "</div>";
			mailService.sendMail(cust.getEmail(), subject, body);
			log.info("Customer Saved Successfully");
			return new ApiResponse(true, "Customer Saved Successfully", cust, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured While Saving Customer");
			return new ApiResponse(false, "Exception Occured While Saving Customer", null,
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse loginCustomer(CustomerLoginPayload customerdto) {
		try {
			log.info("Login Initiated");
			Customer customer = customerRepository.findCustomerByUsername(customerdto.getUsername())
					.orElseThrow(() -> new NotFoundException("Customer Not Found"));

			if (!encoder.matches(customerdto.getPassword(), customer.getPassword())) {
				log.warn("Incorrect Password");
				return new ApiResponse(false, "Incorrect Password", null, HttpStatus.BAD_REQUEST.value(), null);

			}
			log.info("Login Successfull");
			return new ApiResponse(true, "Login Successfull", customer, HttpStatus.OK.value(), Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured While Fetching Customer");
			return new ApiResponse(false, "Exception Occured While Fetching Customer", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse getCustomerOrderList(Long customerId) {
		try {
			log.info("Retrieving Customer Order List Initiated");
			Optional<Customer> customerOpt = customerRepository.findById(customerId);
			if (customerOpt.isEmpty()) {
				log.warn("Customer Not Found");
				throw new NotFoundException("Customer Not Found");
			}
			log.info("Order List Successfully fetched");
			return new ApiResponse(true, "Order List Successfully fetched", customerOpt.get().getOrderedList(),
					HttpStatus.OK.value(), Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured While Fetching Customer Order List");
			return new ApiResponse(false, "Exception Occured While Fetching Customer Order List", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}


	public ApiResponse updateCustomerDetails(CustomerPayload customerPayload, Long customerId) {
		try {
			log.info("Update Customer Details Initiated");
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new NotFoundException("Customer Not Found"));
			if (!customer.getEmail().equals(customerPayload.getEmail())
					&& customerRepository.existsByEmail(customerPayload.getEmail())) {
				log.warn("Updated emailId is linked with an Other Account");
				return new ApiResponse(false, "Update in Customer Email Failed", "Email is linked with Another Account",
						HttpStatus.BAD_REQUEST.value(), null);
			}
			if (!customer.getUsername().equals(customerPayload.getUsername())
					&& customerRepository.existsByUsername(customerPayload.getUsername())) {
				log.warn("Updated Username is linked with an Other Account");
				return new ApiResponse(false, "Update in Customer Username Failed",
						"Username is linked with Another Account", HttpStatus.BAD_REQUEST.value(), null);
			}
			if (!customer.getMobileNo().equals(customerPayload.getMobileNo())
					&& customerRepository.existsByMobileNo(customerPayload.getMobileNo())) {
				log.warn("Updated Mobile No is linked with an Other Account");
				return new ApiResponse(false, "Update in Mobile No Failed",
						"Mobile No is linked with Another Account", HttpStatus.BAD_REQUEST.value(), null);
			}
			customer.setEmail(customerPayload.getEmail());
			customer.setMobileNo(customerPayload.getMobileNo());
			customer.setPassword(customerPayload.getPassword());
			customer.setUsername(customerPayload.getUsername());
			customer.setName(customerPayload.getName());
			Customer savedCustomer = customerRepository.save(customer);
			log.info("Customer Details Updated Successfully");
			return new ApiResponse(true, "Customer Updated Successfully", savedCustomer, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured" + e.getMessage());
			return new ApiResponse(false, "Exception While Updating Customer", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse deleteCustomer(Long customerId) {
		try {
			log.info("");
			Optional<Customer> fetchedCustomer = customerRepository.findById(customerId);
			if (fetchedCustomer.isEmpty()) {
				log.warn("Customer Not Found");
				return new ApiResponse(false, "Customer Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			customerRepository.delete(fetchedCustomer.get());
			return new ApiResponse(true, "Customer Deleted Successfully", null, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured" + e.getMessage());
			return new ApiResponse(false, "Exception While Deleting Customer", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

}
