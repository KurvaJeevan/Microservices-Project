package com.gc.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.dto.ApiResponse;
import com.gc.entity.Address;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.entity.Product;
import com.gc.exception.InvalidException;
import com.gc.exception.NotFoundException;
import com.gc.feignclient.ShippingClient;
import com.gc.payload.DeleteOrderPayload;
import com.gc.payload.OrderPayload;
import com.gc.payload.ShippingPayload;
import com.gc.repository.CustomerRepository;
import com.gc.repository.OrderRepository;
import com.gc.repository.ProductRepository;
import com.gc.utils.MailTemplateUtill;
import com.gc.utils.PdfGenerator;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private MailService mailService;
	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private CustomerRepository customerRepository;
	private ShippingClient shippingClient;
	private ObjectMapper objectMapper;
	private MailTemplateUtill mailTemplateUtill = new MailTemplateUtill();

	public OrderService(MailService mailService, OrderRepository orderRepository, ProductRepository productRepository,
			CustomerRepository customerRepository, ShippingClient shippingClient, ObjectMapper objectMapper) {
		super();
		this.mailService = mailService;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.shippingClient = shippingClient;
		this.objectMapper = objectMapper;
	}

	public ApiResponse saveOrder(OrderPayload orderPayload, Long customerId) {
		try {
			log.info("Saving Order Initiated");
			if (!validateAddress(orderPayload.getAddress())) {
				log.warn("Failed While Validating the Address");
				return new ApiResponse(false, "Address Incorrect", "Failed While Validating the Address",
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			Product product = getProduct(orderPayload.getProductId());
			Customer customer = getLoggedInCustomer(customerId);

			double ordervalue = product.getProductPrice() * orderPayload.getOrderQuantity();
			Order placedOrder = new Order(null, "Placed", LocalDateTime.now(), orderPayload.getOrderQuantity(),
					ordervalue, product, orderPayload.getAddress(), null, customer.getUsername());

			Order savedOrder = orderRepository.save(placedOrder);
			addOrderToCustomer(savedOrder, customer);
			sendOrderMail(savedOrder, customer, null, true);
			processShipping(savedOrder, customer);
			log.info("Order Saved Successfully");
			return buildResponse(true, "Order Saved Successfully", savedOrder);
		} catch (Exception e) {
			log.error("Failed to Save Order");
			return buildErrorResponse("Failed to Save Order", e);
		}
	}

	public ApiResponse getOrderById(Long id) {

		try {
			log.info("Getting Order Initiated");
			Order order = getOrderByIdOrThrow(id);
			log.info("Order Found Successfully");
			return buildResponse(true, "Order Found Successfully", order);
		} catch (Exception e) {
			log.error("Failed to Fetch Order");
			return buildErrorResponse("Failed to Fetch Order", e);
		}
	}

	public ApiResponse getAllOrders() {
		try {
			log.info("Fetching OrderList Initiated");
			List<Order> orderList = orderRepository.findAll();
			if (orderList.isEmpty()) {
				log.warn("Empty Order List");
				return new ApiResponse(false, "Empty Order List", orderList, HttpStatus.OK.value(),
						Collections.emptyList());
			} else {
				log.info("Successfully Fetched All Orders");
				return buildResponse(true, "All Orders", orderList);
			}
		} catch (Exception e) {
			log.error("Failed to Fetch Orders");
			return buildErrorResponse("Failed to Fetch Orders", e);
		}
	}

	public void changeOrderStatus(String status, Long orderid) {
		log.info("Change Order Status Initated");
		Order order = getOrderByIdOrThrow(orderid);
		order.setOrderStatus(status);
		orderRepository.save(order);
		log.info("Change Order Status Successful");
	}

	public ApiResponse changeShippingStatus(Long orderId, Long customerId) {
		try {
			log.info("Changing Shipping Status Initiated");
			Customer customer = getLoggedInCustomer(customerId);

			Order order = getOrderByIdOrThrow(orderId);
			ShippingPayload shippingDto = fetchShippingStatus(order);

			if (!shippingDto.getShippingStatus().equals("Shipping Initiated")) {
				log.warn("Shipping Status is Not Yet Initiated");
				return new ApiResponse(false, "Order is already " + shippingDto.getShippingStatus(), null,
						HttpStatus.BAD_REQUEST.value(), null);
			}
			if (!shippingDto.getShippingId().equals(order.getShippingId())) {
				log.warn("Shipping Id Mismatched");
				return new ApiResponse(false, "Shipping ID mismatch", null, HttpStatus.BAD_REQUEST.value(), null);
			}
			order.setOrderStatus("Shipped");
			orderRepository.save(order);
			sendOrderMail(order, customer, shippingDto, false);
			log.info("Shipping Status Changed");
			return buildResponse(true, "Shipping Status Changed", order);
		} catch (Exception e) {
			log.error("Exception Occured While Changing Shipping Status");
			return buildErrorResponse("Exception Occured While Changing Shipping Status", e);
		}
	}

	private ShippingPayload fetchShippingStatus(Order order) {
		log.info("Fetching Shipping Status Initiated");
		ApiResponse response = shippingClient.getStatusOfOrder(order.getOrderId());
		if (!response.isSuccess() || response.getStatusCode() != 200) {
			log.info("Failed to Fetch Shipping Status of Order");
			throw new InvalidException("Failed to fetch shipping status: " + response.getMessage());
		}
		log.info("Shipping Status Fetched Successfully");
		return objectMapper.convertValue(response.getData(), ShippingPayload.class);
	}

	private Order getOrderByIdOrThrow(Long orderId) {
		log.info("Checking Order Exists or Not");
		return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order Not Found"));
	}

	private Product getProduct(Long productid) {
		log.info("Checking Product Exists or Not");
		return productRepository.findById(productid).orElseThrow(() -> new NotFoundException("Product Not Found"));
	}

	private Customer getLoggedInCustomer(Long customerId) {
		log.info("Finding Customer Initiated");
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			log.info("Customer Not Found");
			throw new NotFoundException("Customer Not Found");
		}
		log.info("Customer Found");
		return customer.get();
	}

	private ApiResponse buildResponse(boolean success, String message, Object data) {
		return new ApiResponse(success, message, data, HttpStatus.OK.value(), Collections.emptyList());
	}

	private ApiResponse buildErrorResponse(String message, Exception e) {
		return new ApiResponse(false, message, e.getMessage(), HttpStatus.BAD_REQUEST.value(),
				Collections.singletonList(e.getMessage()));
	}

	private void addOrderToCustomer(Order order, Customer customer) {
		List<Order> orderList = Optional.ofNullable(customer.getOrderedList()).orElseGet(ArrayList::new);
		orderList.add(order);
		customer.setOrderedList(orderList);
		customerRepository.save(customer);
	}

	private boolean validateAddress(Address address) {
		log.info("Address Validation Initiated");
		if (!shippingClient.validateShippingAddress(address)) {
			log.error("Address is Incorrect");
			return false;

		}
		log.error("Address Validated");
		return true;
	}

	private void processShipping(Order order, Customer customer) {
		log.info("Shipping Process Initiated");
		ShippingPayload shippingDto = new ShippingPayload(null, order.getOrderId(), customer.getId(), null);
		ApiResponse shippingResponse = shippingClient.saveShipping(shippingDto);

		ShippingPayload savedShippingDto = objectMapper.convertValue(shippingResponse.getData(), ShippingPayload.class);
		order.setShippingId(savedShippingDto.getShippingId());
		orderRepository.save(order);
		log.info("Shipping Process Completed");

	}

	private void sendOrderMail(Order order, Customer customer, ShippingPayload shippingDto, boolean isConfirmation) {
		try {
			log.info("Sending Order Mail Initiated");
			String subject = isConfirmation ? "🎉 Your Order #" + order.getOrderId() + " is Confirmed!"
					: "Your Order #" + order.getOrderId() + " Has Been Shipped! 🚚";

			String body = isConfirmation
					? (String) mailTemplateUtill.buildOrderConfirmationMail(customer, order).getData()
					: (String) mailTemplateUtill.buildOrderShippedMail(customer, order, shippingDto).getData();
			if (isConfirmation) {
				log.info("Sending Mail for Order Confirmation");
				byte[] pdfReceipt = (byte[]) PdfGenerator.generateOrderReceipt(order, customer).getData();

				mailService.sendMailWithAttachment(customer.getEmail(), subject, body, pdfReceipt,
						"Receipt_" + order.getOrderId() + ".pdf");
			} else {
				log.info("Sending mail for shipping status");
				mailService.sendMail(customer.getEmail(), subject, body);
			}
		} catch (MessagingException e) {
			log.error(e.getMessage());
		}
	}

	public ApiResponse deleteOrder(DeleteOrderPayload deleteOrderPayload) {
		try {
			log.info("Deleting Order Initiated");
			Optional<Customer> fetchedCustomer = customerRepository.findById(deleteOrderPayload.getCustomerId());
			if (fetchedCustomer.isEmpty()) {
				log.warn("Customer Not Found");
				return new ApiResponse(false, "Customer Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			Customer customer = fetchedCustomer.get();

			List<Order> orderedList = customer.getOrderedList();
			if (orderedList == null) {
				orderedList = new ArrayList<Order>();
			}

			for (Order o : orderedList) {
				if (o.getOrderId().equals(deleteOrderPayload.getOrderId())) {
					orderedList.remove(o);
					customer.setOrderedList(orderedList);
					customerRepository.save(customer);
					orderRepository.deleteById(deleteOrderPayload.getOrderId());
					log.info("Order Deleted Successfully");
					return new ApiResponse(true, "Order Deleted Successfully", null, HttpStatus.OK.value(),
							Collections.emptyList());
				}
			}
			log.info("Order Not Found in Customer's Order List");
			return new ApiResponse(false, "Order Not Found in Customer's Order List", null, HttpStatus.BAD_REQUEST.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured"+e.getMessage());
			return new ApiResponse(false, "Exception While Deleting Order", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse updateOrder(OrderPayload orderPayload, Long orderId) {
		try {
			log.info("Updating order Initiated");

			Optional<Order> fetchedOrder = orderRepository.findById(orderId);
			if (fetchedOrder.isEmpty()) {
				log.warn("Order Not Found");
				return new ApiResponse(false, "Order Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			Order order = fetchedOrder.get();
			String orderStatus = order.getOrderStatus();
			if (!orderStatus.equals("Placed")) {
				log.warn("Order Update Failed as order status is:" + orderStatus);
				return new ApiResponse(false, "Order Update Failed as order status is:" + orderStatus, null,
						HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
			}
			Optional<Product> fetchedProduct = productRepository.findById(orderPayload.getProductId());
			if (fetchedProduct.isEmpty()) {
				log.warn("Product Not Found");
				return new ApiResponse(false, "Product Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			Product product = fetchedProduct.get();
			order.setOrderId(orderId);
			order.setAddress(orderPayload.getAddress());
			order.setProduct(product);
			order.setOrderValue(product.getProductPrice() * orderPayload.getOrderQuantity());
			order.setOrderQuantity(orderPayload.getOrderQuantity());
			Order savedorder = orderRepository.save(order);
			log.info("Order Updated Successfully");
			return new ApiResponse(true, "Order Updated Successfully", savedorder, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured"+e.getMessage());
			return new ApiResponse(false, "Exception While Updating Order", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}
}
