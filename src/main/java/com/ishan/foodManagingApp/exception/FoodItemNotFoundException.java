package com.ishan.foodManagingApp.exception;

public class FoodItemNotFoundException extends RuntimeException {
    public FoodItemNotFoundException(Integer id) {
        super("Food item not found with ID: " + id);
    }
}