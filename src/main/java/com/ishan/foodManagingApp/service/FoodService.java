package com.ishan.foodManagingApp.service;

import com.ishan.foodManagingApp.DTO.FoodCreateRequest;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
import com.ishan.foodManagingApp.exception.FoodItemNotFoundException;
import com.ishan.foodManagingApp.exception.InvalidImageException;
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
        if (image.isEmpty()) {
            throw new InvalidImageException("Image is required");
        }

        String contentType = image.getContentType();
        if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
            throw new InvalidImageException("Only PNG or JPEG images are allowed.");
        }
        long maxSize = 5 * 1024 * 1024;
        if (image.getSize() > maxSize) {
            throw new InvalidImageException("Image size must be less than 5MB.");
        }

        Food food = new Food();
        food.setFoodName(foodRequest.getFoodName());
        food.setPrice(foodRequest.getPrice());
        food.setCategory(foodRequest.getCategory());
        food.setImageName(image.getOriginalFilename());
        food.setImageType(image.getContentType());
        food.setImageData(image.getBytes());
        repo.save(food);
    }

    public void updateFoodItem(FoodUpdateRequest updatedItem, MultipartFile image) throws IOException {
        Food foodItem = repo.findById(updatedItem.getFoodId())
                .orElseThrow(() -> new FoodItemNotFoundException(updatedItem.getFoodId()));
        foodItem.setFoodName(updatedItem.getFoodName());
        foodItem.setPrice(updatedItem.getPrice());
        foodItem.setCategory(updatedItem.getCategory());
        if (image != null && !image.isEmpty()) {
            foodItem.setImageName(image.getOriginalFilename());
            foodItem.setImageType(image.getContentType());
            foodItem.setImageData(image.getBytes());
        }
        repo.save(foodItem);
    }

    public void deleteFoodItem(Integer foodId) {
        if (!repo.existsById(foodId)) {
            throw new FoodItemNotFoundException(foodId);
        }
        repo.deleteById(foodId);
    }
}