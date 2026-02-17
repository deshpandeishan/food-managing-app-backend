package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepo extends JpaRepository<Food, Integer> {
    @Query("SELECT new com.ishan.foodManagingApp.DTO.FoodResponse(" +
            "f.foodId, f.foodName, f.price, f.category, null, f.imageName, f.imageType) " +
            "FROM Food f WHERE UPPER(f.foodName) LIKE UPPER(CONCAT('%', :query, '%'))")
    Page<FoodResponse> searchFood(@Param("query") String query, Pageable pageable);
}