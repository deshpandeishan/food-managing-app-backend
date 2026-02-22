package com.ishan.foodManagingApp.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.math.BigDecimal;

@JsonPropertyOrder({ "foodName", "quantity", "priceAtTimeOfOrder", "subtotal", "cgstAmount", "sgstAmount", "totalTaxAmount", "totalAmount", "totalAmountAfterTax" })
@Getter
@Setter
public class OrderItemResponse {
    private String foodName;
    private Integer quantity;
    private BigDecimal priceAtTimeOfOrder;
    private BigDecimal subtotal;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountAfterTax;
}