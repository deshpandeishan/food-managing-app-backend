package com.ishan.foodManagingApp.service;

import com.ishan.foodManagingApp.DTO.FoodCreateRequest;
import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
import com.ishan.foodManagingApp.exception.FoodItemNotFoundException;
import com.ishan.foodManagingApp.exception.InvalidImageException;
import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.repository.FoodRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FoodService {

    private final FoodRepo repo;
    public FoodService(FoodRepo repo) {
        this.repo = repo;
    }

    public void addFoodItem(FoodCreateRequest foodRequest, MultipartFile image) throws IOException {
        Food food = new Food();
        food.setFoodName(foodRequest.getFoodName());
        food.setPrice(foodRequest.getPrice());
        food.setCategory(foodRequest.getCategory());

        if (image != null && !image.isEmpty()) {
            food.setImageName(image.getOriginalFilename());
            food.setImageType(image.getContentType());
            food.setImageData(image.getBytes());
        }

        System.out.println("Debug: foodName class: " + food.getFoodName().getClass());
        System.out.println("Debug: price class: " + food.getPrice().getClass());
        System.out.println("Debug: category class: " + food.getCategory().getClass());
        System.out.println("Debug: imageName class: " + (food.getImageName() != null ? food.getImageName().getClass() : "null"));
        System.out.println("Debug: imageType class: " + (food.getImageType() != null ? food.getImageType().getClass() : "null"));
        System.out.println("Debug: imageData class: " + (food.getImageData() != null ? food.getImageData().getClass() : "null"));

        repo.save(food);
        System.out.println("Service: Saved Food entity to DB.");
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
        } else if (updatedItem.getImageData() != null) {
            foodItem.setImageName(updatedItem.getFoodName());
            foodItem.setImageType(updatedItem.getImageType());
            Byte[] boxed = updatedItem.getImageData();
            byte[] unboxed = new byte[boxed.length];
            for (int i = 0; i < boxed.length; i++) {
                unboxed[i] = boxed[i];
            }
            foodItem.setImageData(unboxed);
        }
        repo.save(foodItem);
    }

    public void deleteFoodItem(Integer foodId) {
        if (!repo.existsById(foodId)) {
            throw new FoodItemNotFoundException(foodId);
        }
        repo.deleteById(foodId);
    }

    public Page<FoodResponse> searchAndFilter(String query, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.searchAndFilter(query, category, pageable);
    }
}