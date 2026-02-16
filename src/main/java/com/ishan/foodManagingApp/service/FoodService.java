package com.ishan.foodManagingApp.service;

import com.ishan.foodManagingApp.DTO.FoodCreateRequest;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.repository.FoodRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FoodService {

    private final FoodRepo repo;
    public FoodService(FoodRepo repo) {
        this.repo = repo;
    }

    public List<Food> getFoodItems() {
        return repo.findAll();
    }

    public void addFoodItem(FoodCreateRequest foodRequest, MultipartFile image) throws IOException {
        Food food = new Food();
        food.setFoodName(foodRequest.getFoodName());
        food.setPrice(foodRequest.getPrice());
        food.setCategory(foodRequest.getCategory());
        food.setImageName(image.getOriginalFilename());
        food.setImageType(image.getContentType());
        food.setImageData(image.getBytes());
        repo.save(food);
    }

    public void updateFoodItem(FoodUpdateRequest updatedItem) {
        Food foodItem = repo.findById(updatedItem.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food item not found!"));
        foodItem.setFoodName(updatedItem.getFoodName());
        foodItem.setPrice(updatedItem.getPrice());
        foodItem.setCategory(updatedItem.getCategory());
        repo.save(foodItem);
    }

    public void deleteFoodItem(Integer foodId) {
        repo.deleteById(foodId);
    }
}
