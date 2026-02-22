package com.ishan.foodManagingApp.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({ "orderId", "orderDate", "status", "totalAmount", "totalCgst", "totalSgst", "totalTax", "finalAmount", "items" })
@Getter
@Setter
public class OrderDetailResponse {
    private Integer orderId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal totalCgst;
    private BigDecimal totalSgst;
    private BigDecimal totalTax;
    private BigDecimal finalAmount;
    private List<OrderItemResponse> items;
}