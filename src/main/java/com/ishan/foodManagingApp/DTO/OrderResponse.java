package com.ishan.foodManagingApp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private Integer orderId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private Integer itemCount;
}
