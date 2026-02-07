package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.entity.Product;
import com.gc.feignclient.ShippingClient;
import com.gc.payload.DeleteOrderPayload;
import com.gc.payload.OrderPayload;
import com.gc.payload.ShippingPayload;
import com.gc.repository.CustomerRepository;
import com.gc.repository.OrderRepository;
import com.gc.repository.ProductRepository;
import com.gc.utils.MailTemplateUtill;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class OrderServiceTest {

	@Mock
	private MailService mailService;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private ShippingClient shippingClient;
	@Mock
	private ObjectMapper objectMapper;
	@InjectMocks
	private OrderService orderService;
	@Mock
	private Order order;
	@Mock
	private OrderPayload orderPayload;
	@Mock
	private Address address;
	@Mock
	private Product product;
	@Mock
	private Customer customer;
	@Mock
	private ApiResponse responseStructure;
	@Mock
	private ShippingPayload shippingDto;
	@Mock
	private MailTemplateUtill mailTemplateUtill;


	@Test
	@DisplayName("Success while Saving Order - OrderService")
	void testSaveOrder() {
		OrderPayload orderDTO= new OrderPayload(5L, 5, address);
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ShippingPayload shippingDto= new ShippingPayload(1L, order.getOrderId(), customer.getId(),"Shipping Initiated");
		when(shippingClient.validateShippingAddress(address)).thenReturn(true);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(shippingClient.saveShipping(any(ShippingPayload.class))).thenReturn(new ApiResponse(true,"Success",shippingDto,HttpStatus.OK.value(),null));
		when(objectMapper.convertValue(shippingDto, ShippingPayload.class)).thenReturn(shippingDto);
		ApiResponse rs = orderService.saveOrder(orderDTO,anyLong());
		log.info(rs+"");
		assertEquals(true, rs.isSuccess());
		assertEquals("Order Saved Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Product Not Found Exception -OrderService")
	void testSaveOrderProductNotFoundException() {
		OrderPayload orderDTO= new OrderPayload(5L, 5, address);
		when(shippingClient.validateShippingAddress(address)).thenReturn(true);
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = orderService.saveOrder(orderDTO,anyLong());
		log.info(rs+"");
		assertEquals(false, rs.isSuccess());
		assertEquals("Failed to Save Order", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Success while Saving Order - OrderService")
	void testSaveOrderCustomerNotFoundException() {
		OrderPayload orderDTO= new OrderPayload(5L, 5, address);
		when(shippingClient.validateShippingAddress(address)).thenReturn(true);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = orderService.saveOrder(orderDTO,anyLong());
		log.info(rs+"");
		assertEquals(false, rs.isSuccess());
		assertEquals("Failed to Save Order", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}

	@Test
	@DisplayName("Invalid Order Address")
	void testInvalidOrderAddress() {
		ApiResponse rs = orderService.saveOrder(orderPayload,5L);
		assertEquals("Address Incorrect", rs.getMessage());
		assertEquals(false, rs.isSuccess());
		assertEquals( HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Success Getting Order by id - OrderService")
	void testGetOrderById() {
		when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		ApiResponse rs = orderService.getOrderById(1L);
		assertEquals(order, rs.getData());
	}

	@Test
	@DisplayName("Exception Getting Order by id - OrderService")
	void GetOrderByIdWithException() {
		when(orderRepository.findById(Mockito.anyLong())).thenReturn(null);
		ApiResponse rs = orderService.getOrderById(1L);
		assertEquals("Failed to Fetch Order", rs.getMessage());
		
	}

	@Test
	@DisplayName("Success Getting All Orders - OrderService")
	void testGetAllOrders() {
		when(orderRepository.findAll()).thenReturn(List.of(order));
		ApiResponse rs = orderService.getAllOrders();
		assertEquals(List.of(order), rs.getData());
	}

	@Test
	@DisplayName("Exception Getting All orders - OrderService")
	void testGetAllOrdersError() {
		when(orderRepository.findAll()).thenReturn(null);
		ApiResponse rs = orderService.getAllOrders();
		assertEquals("Failed to Fetch Orders", rs.getMessage());
	}
	@Test
	@DisplayName("Testing Empty Order List - OrderService")
	void testGetAllOrdersEmpty() {
		when(orderRepository.findAll()).thenReturn(Collections.emptyList());
		ApiResponse rs = orderService.getAllOrders();
		assertEquals(Collections.emptyList(), rs.getData());
	}
	
	@Test
	@DisplayName("Success Changing Order Status - OrderService")
	void testChangeOrderStatus() {
		when(orderRepository.save(order)).thenReturn(order);
		when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		orderService.changeOrderStatus("Shipped", order.getOrderId());
		verify(orderRepository,times(1)).save(order);
	}

	@Test
	@DisplayName("Success Changing Shipping Status - OrderService")
	void testChangeShippingStatus() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ShippingPayload shippingDto= new ShippingPayload(5L, order.getOrderId(), customer.getId(),"Shipping Initiated");
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true,"Success",shippingDto,HttpStatus.OK.value(),null));
		when(objectMapper.convertValue(shippingDto, ShippingPayload.class)).thenReturn(shippingDto);
		ApiResponse rs= orderService.changeShippingStatus(order.getOrderId(),anyLong());
		assertEquals(true, rs.isSuccess());
		assertEquals("Shipping Status Changed", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Order Not Found while Changing Shipping Status - OrderService")
	void testChangeShippingStatusOrderException() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs= orderService.changeShippingStatus(order.getOrderId(),anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Order Not Found", rs.getData());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception at shipping status - OrderService")
	void testChangeShippingStatusException() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true,"Failed",null,HttpStatus.BAD_REQUEST.value(),null));
		ApiResponse rs= orderService.changeShippingStatus(order.getOrderId(),anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception Occured While Changing Shipping Status", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception Shipping Status is not same- OrderService")
	void testChangeShippingStatusDifferentException() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 5L,customer.getUsername());
		ShippingPayload shippingDto= new ShippingPayload(5L, order.getOrderId(), customer.getId(),"Shipping");
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true,"Success",shippingDto,HttpStatus.OK.value(),null));
		when(objectMapper.convertValue(shippingDto, ShippingPayload.class)).thenReturn(shippingDto);
		ApiResponse rs= orderService.changeShippingStatus(order.getOrderId(),anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Order is already " + shippingDto.getShippingStatus(), rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception where Shipping Id Mismatch- OrderService")
	void testChangeShippingStatusOrderIdException() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		ShippingPayload shippingDto= new ShippingPayload(5L, order.getOrderId(), customer.getId(),"Shipping Initiated");
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(shippingClient.getStatusOfOrder(anyLong())).thenReturn(new ApiResponse(true,"Success",shippingDto,HttpStatus.OK.value(),null));
		when(objectMapper.convertValue(shippingDto, ShippingPayload.class)).thenReturn(shippingDto);
		ApiResponse rs= orderService.changeShippingStatus(order.getOrderId(),anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Shipping ID mismatch", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Customer Not found while Deleting Order")
	void testDeleteOrderCustomerException() {
		DeleteOrderPayload deleteOrderPayload= new DeleteOrderPayload();
		deleteOrderPayload.setCustomerId(5L);
		deleteOrderPayload.setOrderId(5L);
		ApiResponse rs = orderService.deleteOrder(deleteOrderPayload);
		assertEquals(false, rs.isSuccess());
		assertEquals("Customer Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Order Not Found while Deleting Order")
	void testDeleteOrderNotFound() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		DeleteOrderPayload deleteOrderPayload= new DeleteOrderPayload();
		deleteOrderPayload.setCustomerId(5L);
		deleteOrderPayload.setOrderId(5L);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		ApiResponse rs = orderService.deleteOrder(deleteOrderPayload);
		assertEquals(false, rs.isSuccess());
		assertEquals("Order Not Found in Customer's Order List", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Exception while Deleting Order")
	void testDeleteOrderException() {
		DeleteOrderPayload deleteOrderPayload= new DeleteOrderPayload();
		deleteOrderPayload.setCustomerId(5L);
		deleteOrderPayload.setOrderId(5L);
		when(customerRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = orderService.deleteOrder(deleteOrderPayload);
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception While Deleting Order", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Order Deleted Successfully - OrderService")
	void testDeleteOrder() {
		Customer customer = new Customer(1L, "Jeevan", "Jeevan@gmail.com", 994957L, "jeevan2", "Jeevan12", null);
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		Order order2 = new Order(2L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		ArrayList<Order> orderList= new ArrayList<Order>();
		orderList.add(order2);
		orderList.add(order);
		customer.setOrderedList(orderList);
		DeleteOrderPayload deleteOrderPayload= new DeleteOrderPayload();
		deleteOrderPayload.setCustomerId(5L);
		deleteOrderPayload.setOrderId(2L);
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		ApiResponse rs = orderService.deleteOrder(deleteOrderPayload);
		assertEquals(true, rs.isSuccess());
		assertEquals("Order Deleted Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Successfully Updated Order - OrderService")
	void testUpdateOrder() {
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		ApiResponse rs = orderService.updateOrder(orderPayload,anyLong());
		assertEquals(true, rs.isSuccess());
		assertEquals("Order Updated Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Order Not Found while Updated Order - OrderService")
	void testUpdateOrderNotFound() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = orderService.updateOrder(orderPayload,anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Order Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Order Already Shipped while Updated Order - OrderService")
	void testUpdateOrderStatusNotValid() {
		Order order = new Order(1L, "Shipped", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		ApiResponse rs = orderService.updateOrder(orderPayload,anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Order Update Failed as order status is:" + order.getOrderStatus(), rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Prodcut Not Found Exception while Updated Order - OrderService")
	void testUpdateOrderProductNotFound() {
		Order order = new Order(1L, "Placed", LocalDateTime.now(), 5, 500.0, product, address, 2L,customer.getUsername());
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = orderService.updateOrder(orderPayload,anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Product Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Exception while Updated Order - OrderService")
	void testUpdateOrderException() {
		when(orderRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = orderService.updateOrder(orderPayload,anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception While Updating Order", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
}
