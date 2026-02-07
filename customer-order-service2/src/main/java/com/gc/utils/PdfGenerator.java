package com.gc.utils;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpStatus;

import com.gc.dto.ApiResponse;
import com.gc.entity.Customer;
import com.gc.entity.Order;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfGenerator {

	public static ApiResponse generateOrderReceipt(Order order, Customer customer) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 36, 36, 60, 36);
			PdfWriter.getInstance(document, outputStream);

			document.open();

			Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Paragraph title = new Paragraph("ORDER RECEIPT", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(new Paragraph("\n"));

		
			PdfPTable customerTable = new PdfPTable(2);
			customerTable.setWidthPercentage(100);

			customerTable.addCell(makeCell("Customer Name:", true));
			customerTable.addCell(makeCell(customer.getName(), false));

			customerTable.addCell(makeCell("Email:", true));
			customerTable.addCell(makeCell(customer.getEmail(), false));

			document.add(customerTable);
			document.add(new Paragraph("\n"));

			
			PdfPTable orderTable = new PdfPTable(2);
			orderTable.setWidthPercentage(100);

			orderTable.addCell(makeCell("Order ID:", true));
			orderTable.addCell(makeCell(String.valueOf(order.getOrderId()), false));

			orderTable.addCell(makeCell("Product:", true));
			orderTable.addCell(makeCell(order.getProduct().getProductName(), false));

			orderTable.addCell(makeCell("Quantity:", true));
			orderTable.addCell(makeCell(String.valueOf(order.getOrderQuantity()), false));

			orderTable.addCell(makeCell("Order Value:", true));
			orderTable.addCell(makeCell("$" + order.getOrderValue(), false));

			orderTable.addCell(makeCell("Order Status:", true));
			orderTable.addCell(makeCell(order.getOrderStatus(), false));

			document.add(orderTable);
			document.add(new Paragraph("\n"));

		
			PdfPTable shippingTable = new PdfPTable(2);
			shippingTable.setWidthPercentage(100);


			shippingTable.addCell(makeCell("Status:", true));
			shippingTable.addCell(makeCell(order.getOrderStatus(), false));

			shippingTable.addCell(makeCell("Shipping Address:", true));
			shippingTable.addCell(makeCell(order.getAddress().toString(), false));

			document.add(shippingTable);

			document.add(new Paragraph("\n\nThank you for your order!", new Font(Font.FontFamily.HELVETICA, 12)));

			document.close();
			return new ApiResponse(true, "Pdf Generated", outputStream.toByteArray(), HttpStatus.OK.value(), null);

		} catch (Exception e) {
			log.error(e.getMessage());
			
			return new ApiResponse(false, "Failed at Pdf Generated", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private static PdfPCell makeCell(String text, boolean isHeader) {
		Font font = isHeader ? new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
				: new Font(Font.FontFamily.HELVETICA, 12);

		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(8);
		cell.setBorder(Rectangle.BOX);
		cell.setBackgroundColor(isHeader ? new BaseColor(230, 230, 230) : BaseColor.WHITE);
		return cell;
	}
}
