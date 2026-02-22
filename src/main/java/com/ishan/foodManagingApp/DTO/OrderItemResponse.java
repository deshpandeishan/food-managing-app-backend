package com.ishan.foodManagingApp.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {

    private String foodName;
    private Integer quantity;
    private BigDecimal priceAtTimeOfOrder;
    private BigDecimal subtotal;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountAfterTax;
}
