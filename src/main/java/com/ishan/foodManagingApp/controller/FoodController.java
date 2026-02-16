package com.ishan.foodManagingApp.controller;

import com.ishan.foodManagingApp.DTO.ApiResponse;
import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
import com.ishan.foodManagingApp.DTO.FoodCreateRequest;
import com.ishan.foodManagingApp.model.Food;
import com.ishan.foodManagingApp.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Base64;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/api")
public class FoodController {

    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> greet() {
        ApiResponse<String> response = new ApiResponse<>("The app is running", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fooditem")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getFoodItems() {
        List<Food> foods = service.getFoodItems();
        List<FoodResponse> responseData = foods.stream().map(food -> {
            FoodResponse fr = new FoodResponse();
            fr.setFoodId(food.getFoodId());
            fr.setFoodName(food.getFoodName());
            fr.setPrice(food.getPrice());
            fr.setCategory(food.getCategory());
            if (food.getImageData() != null) {
                fr.setImage("data:" + food.getImageType() + ";base64," +
                        Base64.getEncoder().encodeToString(food.getImageData()));
            }
            return fr;
        }).toList();

        ApiResponse<List<FoodResponse>> response = new ApiResponse<>("Food items fetched", HttpStatus.OK.value(), responseData);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/fooditem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> addFoodItem(
            @Valid @RequestPart("data") FoodCreateRequest foodItem,
            @RequestPart("image") MultipartFile image) throws IOException {

        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Image is required", HttpStatus.BAD_REQUEST.value(), null));
        }
        if (!("image/png".equals(image.getContentType()) || "image/jpeg".equals(image.getContentType()))) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Only PNG or JPEG images are allowed.", HttpStatus.BAD_REQUEST.value(), null));
        }
        if (image.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Image size must be less than 5MB.", HttpStatus.BAD_REQUEST.value(), null));
        }

        service.addFoodItem(foodItem, image);
        return ResponseEntity.ok(new ApiResponse<>("Food item added", HttpStatus.OK.value(), null));
    }


    @PutMapping("/fooditem")
    public ResponseEntity<ApiResponse<String>> updateFoodItem(
            @Valid @RequestPart("data") FoodUpdateRequest updatedItem,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
                return ResponseEntity.badRequest().body(new ApiResponse<>("Only PNG or JPEG images are allowed.", HttpStatus.BAD_REQUEST.value(), null));
            }
            if (image.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(new ApiResponse<>("Image size must be less than 5MB.", HttpStatus.BAD_REQUEST.value(), null));
            }
        }

        service.updateFoodItem(updatedItem, image);
        return ResponseEntity.ok(new ApiResponse<>("Items details updated", HttpStatus.OK.value(), null));
    }

    @DeleteMapping("/fooditem/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable("id") Integer foodId) {
        service.deleteFoodItem(foodId);
        return ResponseEntity.ok(new ApiResponse<>("Food item deleted", HttpStatus.OK.value(), null));
    }

}