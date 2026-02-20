package com.ishan.foodManagingApp.exception;

public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(Integer orderId) {
        super("Cannot cancel order with id: " + orderId + ", because it is already being delivered or canceled.");
    }
}
