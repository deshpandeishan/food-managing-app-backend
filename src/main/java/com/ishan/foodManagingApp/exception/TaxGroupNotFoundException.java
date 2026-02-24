package com.ishan.foodManagingApp.exception;

public class TaxGroupNotFoundException extends RuntimeException{
    public TaxGroupNotFoundException(String message) {
        super(message);
    }
}
