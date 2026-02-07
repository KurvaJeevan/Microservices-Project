package com.gc.quartzscheduleservice;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.repository.CustomerRepository;
import com.gc.service.MailService;
import com.gc.service.OrderService;
import com.gc.utils.MailTemplateUtill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OrderStatusJob extends QuartzJobBean {

	private final OrderService orderService;
	private MailService mailService;
	private CustomerRepository customerRepository;
	private MailTemplateUtill mailTemplateUtill;

	public OrderStatusJob(OrderService orderService, MailService mailService, CustomerRepository customerRepository,
			MailTemplateUtill mailTemplateUtill) {
		super();
		this.orderService = orderService;
		this.mailService = mailService;
		this.customerRepository = customerRepository;
		this.mailTemplateUtill = mailTemplateUtill;
	}



	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("Order Status Job Execution Initiated");
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			Long orderId = jobDataMap.getLong("orderId");
			String newStatus = jobDataMap.getString("status");
			orderService.changeOrderStatus(newStatus, orderId);
			Long customerId = jobDataMap.getLong("customerId");
			Customer cust = customerRepository.findById(customerId).get();
			Order order = (Order) orderService.getOrderById(orderId).getData();

			mailService.sendMail(cust.getEmail(), "Status of the Order has been changed to " + newStatus,
					(String) mailTemplateUtill.buildOrderStatusChangedMail(cust, order, newStatus).getData());

			log.info("Order " + orderId + " status updated to " + newStatus);
		} catch (Exception e) {
			log.error("Error While Sending Email to customer for status change");
		}
	}

}
