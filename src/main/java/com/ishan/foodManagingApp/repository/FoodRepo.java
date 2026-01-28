package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepo extends JpaRepository<Food, Integer> {
}
