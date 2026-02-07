package com.gc.quartzscheduleservice;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.exception.InvalidException;
import com.gc.exception.NotFoundException;
import com.gc.feignclient.ShippingClient;
import com.gc.payload.ShippingPayload;
import com.gc.repository.CustomerRepository;
import com.gc.repository.OrderRepository;
import com.gc.service.MailService;
import com.gc.utils.MailTemplateUtill;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingJob extends QuartzJobBean {

	private ShippingClient shippingClient;
	private OrderRepository orderRepository;
	private MailService mailService;
	private CustomerRepository customerRepository;
	private MailTemplateUtill mailTemplateUtill;

	public ShippingJob(ShippingClient shippingClient, OrderRepository orderRepository, MailService mailService,
			CustomerRepository customerRepository, MailTemplateUtill mailTemplateUtill) {
		super();
		this.shippingClient = shippingClient;
		this.orderRepository = orderRepository;
		this.mailService = mailService;
		this.customerRepository = customerRepository;
		this.mailTemplateUtill = mailTemplateUtill;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("Shipping Job Execution Initiated");
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			Long orderId = jobDataMap.getLong("orderId");
			Long customerId = jobDataMap.getLong("customerId");
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new NotFoundException("Customer Not found"));

			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new NotFoundException("Order Not found Exception"));
			ShippingPayload shippingDto = fetchShippingStatus(order);
			
			if (!shippingDto.getShippingStatus().equals("Shipping Initiated")) {
				log.error("Order is already "+shippingDto.getShippingStatus()+" "+order);
				return;
			}
			if (!shippingDto.getShippingId().equals(order.getShippingId())) {
				log.error("Shipping Id mismatch"+order);
				return ;
			}

			order.setOrderStatus("Shipped");
			orderRepository.save(order);

			mailService.sendMail(customer.getEmail(), "Your Order #" + order.getOrderId() + " Has Been Shipped! 🚚",
					(String)mailTemplateUtill.buildOrderShippedMail(customer, order, shippingDto).getData());
			log.info("Shipping Status changed" + orderId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public ShippingPayload fetchShippingStatus(Order order) {
		log.info("Fetching of Shipping Status Initiated");
		ApiResponse response = shippingClient.getStatusOfOrder(order.getOrderId());
		if (!response.isSuccess() || response.getStatusCode() != 200) {
			log.info("Failed to fetch Shipping Status");
			throw new InvalidException("Failed to fetch shipping status: " + response.getMessage());
		}
		log.info("Shipping Status Fetched Successfully");
		return new ObjectMapper().convertValue(response.getData(), ShippingPayload.class);
	}

}
