package com.gc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gc.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
