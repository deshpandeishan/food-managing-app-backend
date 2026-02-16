package com.ishan.foodManagingApp.controller;
import com.ishan.foodManagingApp.DTO.FoodResponse;
import com.ishan.foodManagingApp.DTO.FoodUpdateRequest;
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
import com.ishan.foodManagingApp.DTO.FoodCreateRequest;

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
        return "The app is running.";
    }

    @GetMapping("/fooditem")
    public ResponseEntity<List<FoodResponse>> getFoodItems() {
        List<Food> foods = service.getFoodItems();
        List<FoodResponse> response = foods.stream()
                .map(food -> {
                    FoodResponse foodresponse = new FoodResponse();
                    foodresponse.setFoodId(food.getFoodId());
                    foodresponse.setFoodName(food.getFoodName());
                    foodresponse.setPrice(food.getPrice());
                    foodresponse.setCategory(food.getCategory());
                    if (food.getImageData() != null) {
                        foodresponse.setImage("data:" + food.getImageType() + ";base64," +
                                java.util.Base64.getEncoder().encodeToString(food.getImageData()));
                    }
                    return foodresponse;
                })
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/fooditem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addFoodItem(
            @Valid
            @RequestPart("data") FoodCreateRequest foodItem,
            @RequestPart("image") MultipartFile image) throws Exception {
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("Image is required");
            }

            String contentType = image.getContentType();
            if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
                return ResponseEntity.badRequest().body("Only PNG or JPEG images are allowed.");
            }

            long maxSize = 5 * 1024 * 1024;
            if (image.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("Image size must be less than 5MB or less.");
            }

            service.addFoodItem(foodItem, image);
            return new ResponseEntity<>("Food item added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/fooditem")
    public ResponseEntity<?> updateFoodItem(@Valid @RequestBody FoodUpdateRequest updatedItem) {
        try {
            service.updateFoodItem(updatedItem);
            return new ResponseEntity<>("Items details updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/fooditem")
    public ResponseEntity<Map<String, String>> deleteItem(@Valid @RequestBody FoodUpdateRequest foodItem) {
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