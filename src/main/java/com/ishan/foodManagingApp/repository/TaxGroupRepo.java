package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.TaxGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxGroupRepo extends JpaRepository<TaxGroup, Integer> {
}
