package com.ishan.foodManagingApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {
    private Integer foodId;
    private String foodName;
    private BigDecimal price;
    private String category;
    private String image;
    private String imageName;
    private String imageType;
}