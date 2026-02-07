package com.gc.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gc.dto.ApiResponse;
import com.gc.entity.Product;
import com.gc.exception.NotFoundException;
import com.gc.payload.ProductPayload;
import com.gc.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	public ApiResponse saveProduct(ProductPayload productPayload) {
		try {
			log.info("Saving Product Initiated");
			if (!productRepository.findProductByProductName(productPayload.getProductName()).isEmpty()) {
				log.warn("Product Name Already Exists");
				return new ApiResponse(false, "Product Name Already Exists", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			Product product = Product.builder().productName(productPayload.getProductName())
					.productPrice(productPayload.getProductPrice()).createdAt(LocalDateTime.now()).build();
			Product savedProduct = productRepository.save(product);
			log.info("Product Saved Successfully");
			return new ApiResponse(true, "Product Saved Successfully", savedProduct, HttpStatus.CREATED.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Failed to Save Product");
			return new ApiResponse(false, "Failed to Save Product", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse getProductById(Long id) {

		try {
			log.info("Getting Product By Id Initiated");
			Product product = productRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Product Not Found"));
			log.info("Product Found Successfully");
			return new ApiResponse(true, "Product Found Successfully", product, HttpStatus.OK.value(), null);
		} catch (Exception e) {
			log.error("Failed to Fetch Product");
			return new ApiResponse(false, "Failed to Fetch Product", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse getAllProducts() {
		try {
			log.info("Getting All Products Initiated");
			List<Product> productList = productRepository.findAll();
			if (productList.isEmpty()) {
				log.warn("Empty Product List");
				return new ApiResponse(false, "Empty Product List", productList, HttpStatus.OK.value(),
						Collections.emptyList());
			} else {
				log.info("Retrieved All Products");
				return new ApiResponse(true, "All Products", productList, HttpStatus.OK.value(),
						Collections.emptyList());
			}
		} catch (Exception e) {
			log.error("Failed to Fetch Products");
			return new ApiResponse(false, "Failed to Fetch Products", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse updateProduct(ProductPayload productPayload, Long productId) {
		try {
			log.info("Product Update Initiated");
			Optional<Product> fetchedProduct = productRepository.findById(productId);
			if (fetchedProduct.isEmpty()) {
				log.warn("Product Not Found");
				return new ApiResponse(false, "Product Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			Product product = fetchedProduct.get();
			product.setProductName(productPayload.getProductName());
			product.setProductPrice(productPayload.getProductPrice());
			
			Product savedProduct = productRepository.save(product);
			log.info("Product Updated Successfully");
			return new ApiResponse(true, "Product Updated Successfully", savedProduct, HttpStatus.OK.value(), null);
		} catch (Exception e) {
			log.error("Exception Occured"+e.getMessage());
			return new ApiResponse(false, "Exception While Updating product", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse deleteProduct(Long productId) {
		try {
			log.info("Deleting Product Initiated");
			Optional<Product> fetchedProduct= productRepository.findById(productId);
			if(fetchedProduct.isEmpty()) {
				log.warn("Product Not Found");
				return new ApiResponse(false, "Product Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			productRepository.deleteById(productId);
			log.info("Product Deleted Successfully");
			return new ApiResponse(true, "Product Deleted Successfully", null, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured"+e.getMessage());
			return new ApiResponse(false, "Exception While Deleting Product", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}

}
