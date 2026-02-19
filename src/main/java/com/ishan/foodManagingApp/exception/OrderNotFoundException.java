package com.ishan.foodManagingApp.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Integer orderId) {
        super("Order not found with id: " + orderId);
    }
}
