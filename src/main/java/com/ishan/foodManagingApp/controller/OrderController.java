package com.ishan.foodManagingApp.controller;

import com.ishan.foodManagingApp.DTO.ApiResponse;
import com.ishan.foodManagingApp.DTO.OrderCreateRequest;
import com.ishan.foodManagingApp.DTO.OrderDetailResponse;
import com.ishan.foodManagingApp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
