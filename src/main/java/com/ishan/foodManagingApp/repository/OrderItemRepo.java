package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
}
