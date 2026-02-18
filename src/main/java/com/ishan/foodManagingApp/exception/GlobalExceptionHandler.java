package com.ishan.foodManagingApp.exception;

import com.ishan.foodManagingApp.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FoodItemNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleFoodNotFound(FoodItemNotFoundException exception) {
        ApiResponse<Object> errorResponse = new ApiResponse<>(exception.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FoodValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleFoodValidation(FoodValidationException exception) {
        ApiResponse<Object> errorResponse = new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidImage(InvalidImageException exception) {
        ApiResponse<Object> errorResponse = new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseError(DatabaseOperationException exception) {
        ApiResponse<Object> errorResponse = new ApiResponse<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiResponse<Object> response = new ApiResponse<>(message, HttpStatus.BAD_REQUEST.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}