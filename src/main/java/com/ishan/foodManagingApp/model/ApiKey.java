package com.ishan.foodManagingApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer apiId;
    @Column(name = "client_name", nullable = false)
    private String clientName;
    @Column(name = "api_key", nullable = false, unique = true)
    private String apiKey;
    @Column(name = "is_active")
    private boolean isActive = true;
}
