package com.ishan.foodManagingApp.service;

import com.ishan.foodManagingApp.DTO.*;
import com.ishan.foodManagingApp.exception.FoodItemNotFoundException;
import com.ishan.foodManagingApp.exception.OrderNotFoundException;
import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.model.Order;
import com.ishan.foodManagingApp.model.OrderItem;
import com.ishan.foodManagingApp.model.OrderStatus;
import com.ishan.foodManagingApp.repository.FoodRepo;
import com.ishan.foodManagingApp.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final FoodRepo foodrepo;

    public OrderService(OrderRepo orderRepo, FoodRepo foodRepo) {
        this.orderRepo = orderRepo;
        this.foodrepo = foodRepo;
    }

    @Transactional
    public OrderDetailResponse createOrder(OrderCreateRequest request) {

        Order order = new Order();

        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(int i = 0; i < request.getItems().size(); i++) {
            OrderItemRequest itemRequest = request.getItems().get(i);
            Food food = foodrepo.findById(itemRequest.getFoodId()) .orElseThrow(() -> new FoodItemNotFoundException(itemRequest.getFoodId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(food);
            orderItem.setQuantity(itemRequest.getQuantity());

            BigDecimal price = food.getPrice();
            orderItem.setPriceAtTimeOfOrder(price);

            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            totalAmount = totalAmount.add(subtotal);
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setOrderDate(savedOrder.getOrderDate());
        response.setStatus(savedOrder.getOrderStatus().name());
        response.setTotalAmount(savedOrder.getTotalAmount());

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (int i = 0; i < savedOrder.getOrderItems().size(); i++) {
            OrderItem item = savedOrder.getOrderItems().get(i);
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setFoodName(item.getFood().getFoodName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtTimeOfOrder(item.getPriceAtTimeOfOrder());
            itemResponse.setSubtotal(item.getSubtotal());

            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);
        return response;
    }

    public OrderDetailResponse getOrderById(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getOrderStatus().name());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setFoodName(item.getFood().getFoodName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtTimeOfOrder(item.getPriceAtTimeOfOrder());
            itemResponse.setSubtotal(item.getSubtotal());
            items.add(itemResponse);
        }
        response.setItems(items);
        return response;
    }

    public Page<OrderListResponse> getOrdersList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepo.findAll(pageable);

        return ordersPage.map(order -> {
            OrderListResponse response = new OrderListResponse();
            response.setOrderId(order.getOrderId());
            response.setOrderDate(order.getOrderDate());
            response.setStatus(order.getOrderStatus().name());
            response.setTotalAmount(order.getTotalAmount());
            response.setItemCount(order.getOrderItems().size());
            return response;
        });
    }

    public OrderDetailResponse cancelOrder(Integer orderId) {

        Order order = orderRepo.findById(orderId) .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getOrderStatus() == OrderStatus.CANCELED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel and order which has been already canceled or delivered.");
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepo.save(order);
        return getOrderById(orderId);
    }

}
