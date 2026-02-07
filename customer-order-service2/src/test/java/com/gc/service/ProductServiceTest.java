package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import com.gc.dto.ApiResponse;
import com.gc.entity.Product;
import com.gc.payload.ProductPayload;
import com.gc.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;
	@Mock
	private Product product;
	@Mock
	private ProductPayload productPayload;
	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("Success Saving Product - ProductService")
	void testSaveProduct() {
		Product product = new Product(1L, "Headphone", 500.0,LocalDateTime.now());
		when(productRepository.save(any(Product.class))).thenReturn(product);
		ApiResponse rs = productService.saveProduct(productPayload);
		log.info(rs + "");
		assertEquals(product, rs.getData());
		assertEquals("Product Saved Successfully", rs.getMessage());
		assertEquals(HttpStatus.CREATED.value(), rs.getStatusCode());
		assertEquals(true,rs.isSuccess());
		assertEquals(Collections.emptyList(), rs.getErrors());
	}
	
	@Test
	@DisplayName("Testing to Save Duplicate Product- ProductService")
	void testDuplicateProduct() {
		ProductPayload productPayload= new ProductPayload("Headphones", 500.0);
		when(productRepository.findProductByProductName(productPayload.getProductName())).thenReturn(Optional.of(product));
		
		ProductPayload productPayload2= new ProductPayload("Headphones", 500.0);
		ApiResponse rs = productService.saveProduct(productPayload2);
		log.info(rs + "");
		assertEquals("Product Name Already Exists", rs.getMessage());
	}
	
	@Test
	@DisplayName("Exception Saving Invalid Product - ProductService")
	void testInvalidProduct() {
		when(productRepository.save(null)).thenReturn(product);
		ApiResponse rs = productService.saveProduct(productPayload);
		log.info(rs + "");
		assertEquals("Failed to Save Product", rs.getMessage());
	}

	@Test
	@DisplayName("Success Getting Product by Id - ProductService")
	void testGetProductById() {

		when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
		ApiResponse rs = productService.getProductById(1L);
		log.info(rs + "");
		assertEquals(product, rs.getData());
	}

	@Test
	@DisplayName("Exception while Getting Product by Id - ProductService")
	void GetProductByIdWithException() {
		Long productId=5L;
		when(productRepository.findById(1L)).thenReturn(null);
		ApiResponse rs = productService.getProductById(productId);
		assertEquals("Failed to Fetch Product", rs.getMessage());
	}
	
	@Test
	@DisplayName("Exception if Product Not Found - ProductService")
	void GetProductByIdWithProductNotFound() {
		Long productId=5L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());
		ApiResponse rs = productService.getProductById(productId);
		assertEquals("Product Not Found", rs.getData());
	}

	@Test
	@DisplayName("Success Getting All Products - ProductService")
	void testGetAllProducts() {
		when(productRepository.findAll()).thenReturn(List.of(product));
		ApiResponse rs = productService.getAllProducts();
		log.info(rs + "");
		assertEquals(List.of(product), rs.getData());
	}
	
	@Test
	@DisplayName("Exception while Getting All Products - ProductService")
	void testGetAllProductsError() {
		when(productRepository.findAll()).thenReturn(null);
		ApiResponse rs = productService.getAllProducts();
		log.info(rs + "");
		assertEquals("Failed to Fetch Products", rs.getMessage());
	}
	
	@Test
	@DisplayName("Checking if Product List is Empty - ProductService")
	void testGetAllProductsEmpty() {
		when(productRepository.findAll()).thenReturn(Collections.emptyList());
		ApiResponse rs = productService.getAllProducts();
		log.info(rs + "");
		assertEquals(Collections.emptyList(), rs.getData());
	}
	@Test
	@DisplayName("Testing Product Update - ProductService")
	void testUpdateProduct() {
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(productRepository.save(any(Product.class))).thenReturn(product);
		ApiResponse rs = productService.updateProduct(productPayload, anyLong());
		assertEquals(true, rs.isSuccess());
		assertEquals("Product Updated Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(),rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Exception Testing Product Update - ProductService")
	void testUpdateProductException() {
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = productService.updateProduct(productPayload, anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Product Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(),rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception2 Testing Product Update - ProductService")
	void testUpdateProductException2() {
		when(productRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = productService.updateProduct(productPayload, anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception While Updating product", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(),rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Success Deleting Product")
	void testDeleteProduct() {
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		ApiResponse rs = productService.deleteProduct(anyLong());
		assertEquals(true, rs.isSuccess());
		assertEquals("Product Deleted Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(),rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception while Deleting Product")
	void testDeleteProductException() {
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		ApiResponse rs = productService.deleteProduct(anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Product Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(),rs.getStatusCode());
	}
	
	@Test
	@DisplayName("Exception2 while Deleting Product")
	void testDeleteProductException2() {
		when(productRepository.findById(anyLong())).thenReturn(null);
		ApiResponse rs = productService.deleteProduct(anyLong());
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception While Deleting Product", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(),rs.getStatusCode());
	}
}
