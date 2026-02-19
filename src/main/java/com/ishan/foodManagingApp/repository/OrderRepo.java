package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    Page<Order> findAll(Pageable pageable);
}
