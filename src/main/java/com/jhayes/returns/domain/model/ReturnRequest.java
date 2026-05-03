package com.jhayes.returns.domain.model;

// This represents the data coming into the system
public record ReturnRequest(
        String sku,
        int quantity,
        String pickupZip,
        String customerEmail,
        String reasonCode,
        String orderId // <--- Added this to fix the "no such method" error
) {
    // Validate the quantity
    public ReturnRequest {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}