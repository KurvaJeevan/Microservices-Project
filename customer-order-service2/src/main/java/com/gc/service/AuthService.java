//package com.gc.service;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import com.gc.entity.Customer;
//import com.gc.entity.CustomerPrinciples;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class AuthService {
//
//    public Customer getCurrentCustomer() {
//        try {
//			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//			if (principal instanceof CustomerPrinciples) {
//				log.info("Customer Fetched Successfully");
//			    return ((CustomerPrinciples) principal).getCustomer();
//			}
//			log.warn("Failed to Fetch Customer");
//			return null;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//		log.warn("Failed to Fetch Customer");
//
//        return null;
//    }
//}
//
