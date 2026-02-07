package com.gc.utils;

import java.util.Collections;

import org.springframework.http.HttpStatus;

import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.gc.payload.ShippingPayload;

public class MailTemplateUtill {
	public ApiResponse buildOrderConfirmationMail(Customer customer, Order order) {
		String body = """
				<div style='font-family: Arial, sans-serif; max-width:600px; margin:0 auto; padding:20px;
				border:1px solid #e0e0e0; border-radius:8px;'>
				    <h2 style='color:#2E86C1; text-align:center;'>Order Confirmation</h2>
				    <p>Hi <strong>%s</strong>,</p>
				    <p>Thank you for shopping with <strong>CustomerAndOrderService</strong>!
				    Your order is confirmed. 🚀</p>

				    <hr style='border-top:1px solid #e0e0e0;'>

				    <h3>Order Summary</h3>
				    <table style='width:100%%; border-collapse:collapse;'>
				        <tr style='background-color:#f5f5f5;'>
				            <th style='padding:10px;'>Product</th>
				            <th style='padding:10px;'>Qty</th>
				            <th style='padding:10px;'>Price</th>
				        </tr>
				        <tr>
				            <td style='padding:10px;'>%s</td>
				            <td style='padding:10px;'>%d</td>
				            <td style='padding:10px;'>$%.2f</td>
				        </tr>
				    </table>

				    <p><strong>Shipping Address:</strong> %s</p>


				    <hr style='border-top:1px solid #e0e0e0;'>

				    <p style='text-align:center;'>We will notify you once your order is shipped.</p>

				    <p style='text-align:center; color:#888; font-size:12px;'>
				        CustomerAndOrderService Team<br/>
				        © 2025 All rights reserved.
				    </p>
				</div>
				""".formatted(customer.getName(), order.getProduct().getProductName(), order.getOrderQuantity(),
				order.getOrderValue(), order.getAddress());
		return new ApiResponse(true, "Body of Order Confirmation mail", body, HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse buildOrderShippedMail(Customer customer, Order order, ShippingPayload shippingDto) {
		String body = """
				<div style='font-family: Arial; max-width:600px; margin:0 auto; padding:20px;
				border:1px solid #e0e0e0; border-radius:8px;'>
				    <h2 style='color:#2E86C1; text-align:center;'>Order Shipped! 🚚</h2>
				    <p>Hi <strong>%s</strong>,</p>
				    <p>Your order is now shipped and on its way!</p>

				    <hr>

				    <h3>Shipment Details</h3>
				    <p><strong>Shipping ID:</strong> %s</p>
				    <p><strong>Status:</strong> %s</p>
				    <p><strong>Address:</strong> %s</p>

				    <p style='text-align:center; color:#888; font-size:12px;'>
				        CustomerAndOrderService Team<br/>
				        © 2025 All rights reserved.
				    </p>
				</div>
				""".formatted(customer.getName(), shippingDto.getShippingId(), shippingDto.getShippingStatus(),
				order.getAddress());
		return new ApiResponse(true, "Body of Order Shipped mail", body, HttpStatus.OK.value(),
				Collections.emptyList());

	}

	public ApiResponse buildOrderStatusChangedMail(Customer customer, Order order, String newStatus) {
		String body = """
				<div style='font-family: Arial, sans-serif; max-width:600px; margin:0 auto; padding:20px;
				border:1px solid #e0e0e0; border-radius:8px;'>
				    <h2 style='color:#2E86C1; text-align:center;'>Order Status Update</h2>
				    <p>Hi <strong>%s</strong>,</p>
				    <p>Your order <strong>#%d</strong> status has been updated to: <strong>%s</strong> ✅</p>

				    <hr style='border-top:1px solid #e0e0e0;'>

				    <h3>Order Details</h3>
				    <p><strong>Product:</strong> %s</p>
				    <p><strong>Quantity:</strong> %d</p>
				    <p><strong>Total Price:</strong> $%.2f</p>
				    <p><strong>Shipping Address:</strong> %s</p>

				    <hr style='border-top:1px solid #e0e0e0;'>

				    <p style='text-align:center; color:#888; font-size:12px;'>
				        CustomerAndOrderService Team<br/>
				        © 2025 All rights reserved.
				    </p>
				</div>
				""".formatted(customer.getName(), order.getOrderId(), newStatus, order.getProduct().getProductName(),
				order.getOrderQuantity(), order.getOrderValue(), order.getAddress());
		return new ApiResponse(true, "Body of Order Status Changed mail", body, HttpStatus.OK.value(),
				Collections.emptyList());

	}

}
