package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.payload.CustomerLoginPayload;
import com.gc.payload.CustomerPayload;
import com.gc.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	CustomerRepository customerRepository;
	@Mock
	private MailService mailService;
	@InjectMocks
	CustomerService customerService;
	@Mock
	private Customer customer;
	@Mock
	private CustomerPayload customerPayload;
	@Mock
	private CustomerLoginPayload customerLoginDTO;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	@Test
	@DisplayName("Success Registering Customer - CustomerService")
	void testRegisterCustomer() {
		CustomerPayload customerDto = new CustomerPayload("jeevan", "jeevan@gmail.com", "jeevan20", 9949532786L,
				"jeevan123");
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		ApiResponse rs = customerService.registerCustomer(customerDto);
		Customer savedCustomer = (Customer) rs.getData();
		assertEquals(customer.getEmail(), savedCustomer.getEmail());
	}

	@Test
	@DisplayName("Username Already Exists Exception - CustomerService")
	void testRegisterCustomerUsernameException() {
		CustomerPayload customerDto = new CustomerPayload("jeevan", "jeevan@gmail.com", "jeevan20", 9949532786L,
				"jeevan123");
		when(customerRepository.existsByUsername(anyString())).thenReturn(true);
		ApiResponse rs = customerService.registerCustomer(customerDto);
		assertEquals("Username Already Exists Please Try new Username", rs.getMessage());
		assertEquals(false, rs.isSuccess());
	}

	@Test
	@DisplayName("Email Already Exists Exception - CustomerService")
	void testRegisterCustomerEmailException() {
		CustomerPayload customerDto = new CustomerPayload("jeevan", "jeevan@gmail.com", "jeevan20", 9949532786L,
				"jeevan123");
		when(customerRepository.existsByUsername(anyString())).thenReturn(false);
		when(customerRepository.existsByEmail(anyString())).thenReturn(true);
		ApiResponse rs = customerService.registerCustomer(customerDto);
		assertEquals("Customer Already Exists with the Email, Try Login", rs.getMessage());
		assertEquals(false, rs.isSuccess());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}

	@Test
	@DisplayName("Exception while Registering Customer - CustomerService")
	void testRegisterCustomerException() {
		CustomerPayload customerDto = new CustomerPayload("jeevan", "jeevan@gmail.com", "jeevan20", 9949532786L,
				"jeevan123");
		when(customerRepository.save(any(Customer.class))).thenReturn(null);
		ApiResponse rs = customerService.registerCustomer(customerDto);
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception Occured While Saving Customer", rs.getMessage());
	}

	@Test
	@DisplayName("Exception while Logging in Customer - CustomerService")
	void testLoginCustomerException() {
		when(customerRepository.findCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
		ApiResponse rs = customerService.loginCustomer(customerLoginDTO);
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception Occured While Fetching Customer", rs.getMessage());
	}

	@Test
	@DisplayName("Customer Not Found while Logging In - CustomerService")
	void testLoginCustomerNotFound() {
		CustomerLoginPayload customerLoginDTO = new CustomerLoginPayload("jeevan", "jeevan12");

		when(customerRepository.findCustomerByUsername(anyString())).thenReturn(Optional.empty());
		ApiResponse rs = customerService.loginCustomer(customerLoginDTO);
		assertEquals(false, rs.isSuccess());
		assertEquals("Customer Not Found", rs.getData());
	}

	@Test
	@DisplayName("Incorrect Password while Logging In -CustomerService")
	void testLoginCustomerIncorrectPassword() {
		CustomerLoginPayload customerLoginDTO = new CustomerLoginPayload("jeevan", "jeevan12");

		when(customerRepository.findCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
		ApiResponse rs = customerService.loginCustomer(customerLoginDTO);
		assertEquals(false, rs.isSuccess());
		assertEquals("Incorrect Password", rs.getMessage());
	}

	@Test
	@DisplayName("Successfully Logged in -CustomerService")
	void testLoginCustomer() {

		CustomerLoginPayload customerLoginDTO = new CustomerLoginPayload("jeevan", "jeevan12");
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 58456454L, "jeevan", "jeevan12", null);
		customer.setPassword(encoder.encode(customer.getPassword()));
		when(customerRepository.findCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
		ApiResponse rs = customerService.loginCustomer(customerLoginDTO);
		assertEquals(true, rs.isSuccess());
		assertEquals("Login Successfull", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}

	@Test
	@DisplayName("Success- Getting Customers Order List -CustomerService")
	void testGetCustomerOrderList() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		ApiResponse rs = customerService.getCustomerOrderList(anyLong());
		assertEquals("Order List Successfully fetched", rs.getMessage());
	}

	@Test
	@DisplayName("Exception While Getting Customers Order List -CustomerService")
	void testGetCustomerOrderListException() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = customerService.getCustomerOrderList(anyLong());
		assertEquals("Exception Occured While Fetching Customer Order List", rs.getMessage());
	}

	@Test
	@DisplayName("Customer Not Found -CustomerService")
	void testGetCustomerOrderListCustomerException() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = customerService.getCustomerOrderList(anyLong());
		assertEquals("Customer Not Found", rs.getData());
	}

	@Test
	@DisplayName("Successfully updated Customer- CustomerService")
	void testUpdateCustomerDetails() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 58456454L, "jeevan", "jeevan12", null);
		CustomerPayload customerPayload = new CustomerPayload("Jeevan", "Jeevan@gmail.com", "jeevan", 9949532786L,
				"jeevan123");
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		ApiResponse rs = customerService.updateCustomerDetails(customerPayload, anyLong());
		assertEquals("Customer Updated Successfully", rs.getMessage());
		assertEquals(true, rs.isSuccess());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Customer Not Found while updating Customer - CustomerService")
	void testUpdateCustomerDetailsCustomerNotFound() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = customerService.updateCustomerDetails(customerPayload, anyLong());
		assertEquals("Customer Not Found", rs.getData());
		assertEquals(false, rs.isSuccess());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception while updating Customer - CustomerService")
	void testUpdateCustomerDetailsException() {
		when(customerRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = customerService.updateCustomerDetails(customerPayload, anyLong());
		assertEquals("Exception While Updating Customer", rs.getMessage());
		assertEquals(false, rs.isSuccess());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Successfully Deleted Customer- CustomerService")
	void testDeleteCustomerDetails() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		ApiResponse rs = customerService.deleteCustomer(anyLong());
		assertEquals("Customer Deleted Successfully", rs.getMessage());
		assertEquals(true, rs.isSuccess());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Customer Not Found while deleting Customer - CustomerService")
	void testDeleteCustomerDetailsCustomerNotFound() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = customerService.deleteCustomer(anyLong());
		assertEquals("Customer Not Found", rs.getMessage());
		assertEquals(false, rs.isSuccess());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception while deleting Customer - CustomerService")
	void testDeleteCustomerDetailsException() {
		when(customerRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = customerService.deleteCustomer(anyLong());
		assertEquals("Exception While Deleting Customer", rs.getMessage());
		assertEquals(false, rs.isSuccess());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
}
