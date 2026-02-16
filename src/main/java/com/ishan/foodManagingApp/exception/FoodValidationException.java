package com.ishan.foodManagingApp.exception;

public class FoodValidationException extends RuntimeException{
    public FoodValidationException(String message) {
        super(message);
    }
}
