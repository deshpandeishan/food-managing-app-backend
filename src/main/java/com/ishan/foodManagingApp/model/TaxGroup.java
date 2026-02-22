package com.ishan.foodManagingApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taxGroupId;
    private String groupName;

    @OneToMany
    private List<Tax> Taxes;
}
