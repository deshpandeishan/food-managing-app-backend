package com.ishan.foodManagingApp.service;

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

    public void addFoodItem(Food foodItem, MultipartFile image) throws IOException {
        foodItem.setImageName(image.getOriginalFilename());
        foodItem.setImageType(image.getContentType());
        foodItem.setImageData(image.getBytes());
        repo.save(foodItem);
    }

    public void addFoodItems(List<Food> multipleFoodItems) {
        repo.saveAll(multipleFoodItems);
    }

    public void updateFoodItem(Food updatedItem) {
        Food foodItem = repo.findById(updatedItem.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food item not found!"));
        foodItem.setFoodName(updatedItem.getFoodName());
        foodItem.setPrice(updatedItem.getPrice());
        foodItem.setCategory(updatedItem.getCategory());
//        foodItem.setAvailable(updatedItem.getAvailable());
        repo.save(foodItem);
    }

    public void deleteFoodItem(Food foodItem) {
        repo.deleteById(foodItem.getFoodId());
    }
}
