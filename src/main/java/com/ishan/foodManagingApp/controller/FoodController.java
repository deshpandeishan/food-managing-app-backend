package com.ishan.foodManagingApp.controller;


import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
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

    @GetMapping("/fooditem")
    public ResponseEntity<List<Food>> getFoodItems() {
        return new ResponseEntity<>(service.getFoodItems(),HttpStatus.OK);
    }

    @PostMapping(value = "/fooditem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addFoodItem(
            @RequestPart("data") String foodJson,
            @RequestPart("image") MultipartFile image) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Food foodItem = mapper.readValue(foodJson, Food.class);
        try {
            service.addFoodItem(foodItem, image);
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
    public ResponseEntity<Map<String, String>> deleteItem(@RequestBody Food foodItem) {
        try {
            service.deleteFoodItem(foodItem);
            // Return success as JSON
            Map<String, String> response = Collections.singletonMap("message", "Food item deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return error as JSON
            Map<String, String> errorResponse = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
