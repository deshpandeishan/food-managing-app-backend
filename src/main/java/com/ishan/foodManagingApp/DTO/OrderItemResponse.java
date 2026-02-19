package com.ishan.foodManagingApp.DTO;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {

    private String foodName;
    private Integer quantity;
    private BigDecimal priceAtTimeOfOrder;
    private BigDecimal subtotal;
}
