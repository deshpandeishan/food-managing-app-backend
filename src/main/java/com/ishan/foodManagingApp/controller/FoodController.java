package com.ishan.foodManagingApp.controller;


import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodController {


    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String Greet() {
        return "Welcome Sir!";
    }

    @GetMapping("/fooditems")
    public ResponseEntity<List<Food>> getFoodItems() {
        return new ResponseEntity<>(service.getFoodItems(),HttpStatus.OK);
    }

    @PostMapping("/fooditem")
    public ResponseEntity<?> addFoodItem(@RequestBody Food foodItem) {
        try {
            service.addFoodItem(foodItem);
            return new ResponseEntity<>("Food item added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/multiplefooditems")
    public ResponseEntity<?> addFoodItems(@RequestBody List<Food> multipleFoodItems) {
        try {
            service.addFoodItems(multipleFoodItems);
            return new ResponseEntity<>("Food item added", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/fooditem")
    public ResponseEntity<?> updateFoodItem(@RequestBody Food updatedItem) {
        try {
            service.updateFoodItem(updatedItem);
            return new ResponseEntity<>("Items details updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/fooditem")
    public ResponseEntity<?> deleleItem(@RequestBody Food foodItem) {
        try {
            service.deleteFoodItem(foodItem);
            return new ResponseEntity<>("Food item deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
