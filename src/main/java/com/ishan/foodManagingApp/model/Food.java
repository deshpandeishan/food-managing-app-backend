package com.ishan.foodManagingApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodId;
    @NotBlank()
    @Size(max = 100)
    private String foodName;
    @NotNull()
    @Positive()
    private BigDecimal price;
    @NotBlank()
    private String category;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
}
