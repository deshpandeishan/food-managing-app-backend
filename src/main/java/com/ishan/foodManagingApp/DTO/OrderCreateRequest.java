package com.ishan.foodManagingApp.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderCreateRequest {
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
