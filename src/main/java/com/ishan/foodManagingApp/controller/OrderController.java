package com.ishan.foodManagingApp.controller;

import com.ishan.foodManagingApp.DTO.ApiResponse;
import com.ishan.foodManagingApp.DTO.OrderCreateRequest;
import com.ishan.foodManagingApp.DTO.OrderDetailResponse;
import com.ishan.foodManagingApp.DTO.OrderListResponse;
import com.ishan.foodManagingApp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderDetailResponse response = service.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Order Created", HttpStatus.OK.value(), response));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrder(@PathVariable Integer orderId) {
        OrderDetailResponse response = service.getOrderById(orderId);
        return ResponseEntity.ok(new ApiResponse<>("Order details fetched", HttpStatus.OK.value(), response));
    }


    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<OrderListResponse> ordersPage = service.getOrdersList(page, size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("items", ordersPage.getContent());
        responseData.put("currentPage", ordersPage.getNumber());
        responseData.put("totalItems", ordersPage.getTotalElements());
        responseData.put("totalPages", ordersPage.getTotalPages());
        responseData.put("pageSize", ordersPage.getSize());

        return ResponseEntity.ok(new ApiResponse<>("Orders list fetched", HttpStatus.OK.value(), responseData));
    }

}
