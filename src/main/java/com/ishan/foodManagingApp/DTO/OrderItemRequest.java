package com.ishan.foodManagingApp.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    @NotNull
    private Integer foodId;

    @NotNull
    @Positive
    private Integer quantity;
}