package com.gc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gc.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findCustomerByEmail(String email);

	Optional<Customer> findCustomerByUsername(String name);
	
	boolean existsByUsername(String username);
	boolean existsByMobileNo(Long mobileNo);
	boolean existsByEmail(String email);
	boolean existsByUsernameOrEmail(String username, String email);
}
