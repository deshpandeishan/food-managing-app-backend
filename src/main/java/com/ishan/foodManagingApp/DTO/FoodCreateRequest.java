package com.ishan.foodManagingApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class FoodCreateRequest {
    private Integer foodId;
    @NotBlank
    private String foodName;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotBlank
    private String category;
}
