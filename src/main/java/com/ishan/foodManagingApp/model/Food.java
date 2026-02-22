package com.ishan.foodManagingApp.model;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    @Column(name = "image_data", columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] imageData;
    private String imageName;
    private String imageType;

    @ManyToOne
    @JoinColumn(name = "tax_group_id")
    private TaxGroup taxGroup;
}
