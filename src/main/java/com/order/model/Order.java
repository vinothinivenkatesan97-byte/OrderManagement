package com.order.model;

import com.order.exception.InvalidOrderException;

public record Order(
        String orderId, String customerId, Double amount, double vatAmount, String status
) {
    public Order {
        if (amount < 0) {
            throw new InvalidOrderException("Amount cannot be negative.");
        }
        if (vatAmount < 0) {
            throw new InvalidOrderException("VAT amount cannot be negative.");
        }
    }
}
