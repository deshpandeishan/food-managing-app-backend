package com.ishan.foodManagingApp.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Getter
@Setter
public class FoodUpdateRequest {
    @NotNull
    private Integer foodId;
    @NotBlank
    private String foodName;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotBlank
    private String category;
    private String imageName;
    private String imageType;
    @Column(name = "image_data", columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private Byte[] imageData;
}