package com.ishan.foodManagingApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private Integer quantity;
    private BigDecimal priceAtTimeOfOrder;
    private BigDecimal subtotal;

    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalTaxAmount;
    private  BigDecimal totalAmountAfterTax;
}
