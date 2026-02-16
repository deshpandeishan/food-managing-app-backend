package com.ishan.foodManagingApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.awt.image.ReplicateScaleFilter;
import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FoodItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFoodNotFound(FoodItemNotFoundException exception) {
        Map<String, String> errorResponse = Collections.singletonMap("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FoodValidationException.class)
    public ResponseEntity<Map<String, String>> handleFoodValidation(FoodValidationException exception) {
        Map<String, String> errorResponse = Collections.singletonMap("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<Map<String, String>> handleInvalidImage(InvalidImageException exception) {
        Map<String, String> errorResponse = Collections.singletonMap("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseError(DatabaseOperationException exception) {
        Map<String, String> errorResponse = Collections.singletonMap("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}