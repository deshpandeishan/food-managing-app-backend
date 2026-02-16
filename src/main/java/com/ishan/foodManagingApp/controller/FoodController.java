package com.ishan.foodManagingApp.controller;
import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.ishan.foodManagingApp.DTO.FoodRequest;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/api")
public class FoodController {


    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String Greet() {
        return "Welcome! The app is running.";
    }

    @GetMapping("/fooditem")
    public ResponseEntity<List<FoodResponse>> getFoodItems() {
        List<Food> foods = service.getFoodItems();
        List<FoodResponse> response = foods.stream()
                .map(food -> {
                    FoodResponse fr = new FoodResponse();
                    fr.setFoodId(food.getFoodId());
                    fr.setFoodName(food.getFoodName());
                    fr.setPrice(food.getPrice());
                    fr.setCategory(food.getCategory());
                    if (food.getImageData() != null) {
                        fr.setImage("data:" + food.getImageType() + ";base64," +
                                java.util.Base64.getEncoder().encodeToString(food.getImageData()));
                    }
                    return fr;
                })
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/fooditem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addFoodItem(
            @Valid
            @RequestPart("data") FoodRequest foodItem,
            @RequestPart("image") MultipartFile image) throws Exception {
        try {
            service.addFoodItem(foodItem, image);
            return new ResponseEntity<>("Food item added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/fooditem")
    public ResponseEntity<?> updateFoodItem(@Valid @RequestBody FoodRequest updatedItem) {
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
            Map<String, String> response = Collections.singletonMap("message", "Food item deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}