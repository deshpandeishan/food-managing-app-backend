package com.ishan.foodManagingApp.exception;

public class FoodInUseException extends RuntimeException{
    public FoodInUseException(String message) {
        super(message);
    }
}