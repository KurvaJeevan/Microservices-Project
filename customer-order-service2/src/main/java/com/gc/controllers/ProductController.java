package com.gc.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gc.dto.ApiResponse;
import com.gc.payload.ProductPayload;
import com.gc.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/create")
	public ApiResponse saveProduct(@RequestBody ProductPayload productPayload) {
		log.trace("saveProduct called in ProductController");
		return productService.saveProduct(productPayload);
	}
	
	@GetMapping("/{id}")
	public ApiResponse getProductById(@PathVariable Long id) {
		log.trace("getProductById called in ProductController");
		return productService.getProductById(id);
	}

	@GetMapping
	public ApiResponse getAllProducts() {
		log.trace("getAllProducts called in ProductController");
		return productService.getAllProducts();
	}
	
	@PutMapping("/{productId}")
	public ApiResponse updateProduct(@PathVariable Long productId,@RequestBody ProductPayload productPayload) {
		log.trace("getAllProducts called in ProductController");
		return productService.updateProduct(productPayload, productId);
	}
	
	@DeleteMapping("/{productId}")
	public ApiResponse deleteProduct(@PathVariable Long productId) {
		log.trace("deleteProduct called in ProductController");
		return productService.deleteProduct(productId);
	}
}
