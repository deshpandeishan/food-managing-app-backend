package com.ishan.foodManagingApp.controller;

import com.ishan.foodManagingApp.DTO.ApiResponse;
import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
import com.ishan.foodManagingApp.DTO.FoodCreateRequest;
import com.ishan.foodManagingApp.exception.InvalidImageException;
import com.ishan.foodManagingApp.model.Order;
import com.ishan.foodManagingApp.service.FoodService;
import com.ishan.foodManagingApp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin("http://localhost:4200/")
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

    @GetMapping("fooditem/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> getSingleFoodItem(@PathVariable("id") Integer foodId) {
        FoodResponse foodResponse = service.getFoodItemById(foodId);
        return ResponseEntity.ok(new ApiResponse<>("Food item fetched", HttpStatus.OK.value(), foodResponse));
    }

    @GetMapping("/fooditem")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFoodItems(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "10") int page) {
        Page<FoodResponse> pageResult = service.searchAndFilter(query, category, page, size);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("items", pageResult.getContent());
        responseData.put("currentPage", pageResult.getNumber());
        responseData.put("totalItems", pageResult.getTotalElements());
        responseData.put("totalPages", pageResult.getTotalPages());
        responseData.put("pageSize", pageResult.getSize());

        return ResponseEntity.ok(new ApiResponse<>("Fetched food items", HttpStatus.OK.value(), responseData));
    }

    @PostMapping(value = "/fooditem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> addFoodItem(
            @Valid @RequestPart("data") FoodCreateRequest foodItem,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        if (image == null || image.isEmpty()) {
            throw new InvalidImageException("Image is required.");
        }
        if (!("image/png".equals(image.getContentType()) || "image/jpeg".equals(image.getContentType()))) {
            throw new InvalidImageException("Only PNG or JPEG images are allowed.");
        }
        if (image.getSize() > 5 * 1024 * 1024) {
            throw new InvalidImageException("Image size must be less than 5MB.");
        }

        service.addFoodItem(foodItem, image);

        return ResponseEntity.ok(new ApiResponse<>("Food item added", HttpStatus.OK.value(), null));
    }

//    this put mapping requires id inside JSON itself.
    @PutMapping("/fooditem")
    public ResponseEntity<ApiResponse<String>> updateFoodItem(
            @Valid @RequestPart("data") FoodUpdateRequest updatedItem,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
                throw new InvalidImageException("Only PNG or JPEG images are allowed.");
            }
            if (image.getSize() > 5 * 1024 * 1024) {
                throw new InvalidImageException("Image size must be less than 5MB.");
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

    @GetMapping("/fooditems/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchFoodItems(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FoodResponse> resultPage = service.searchAndFilter(query, category, page, size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("items", resultPage.getContent());
        responseData.put("currentPage", resultPage.getNumber());
        responseData.put("totalItems", resultPage.getTotalElements());
        responseData.put("totalPages", resultPage.getTotalPages());
        responseData.put("pageSize", resultPage.getSize());

        return ResponseEntity.ok(new ApiResponse<>("Search results", HttpStatus.OK.value(), responseData));
    }
}