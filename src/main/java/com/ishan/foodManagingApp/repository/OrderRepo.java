package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.Order;
import com.ishan.foodManagingApp.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o")
    Page<Order> findAllOrders(Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Optional<Order> findOrderWithItems(@Param("orderId") Integer orderId);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status")
    Page<Order> findByStatus(@Param("status") OrderStatus status, Pageable pageable);
}