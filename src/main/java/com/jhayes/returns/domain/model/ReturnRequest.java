package com.jhayes.returns.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// This represents the data coming into the system
public record ReturnRequest(
        @NotBlank(message = "SKU is required")
        String sku,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotBlank(message = "Pickup ZIP is required")
        @Pattern(regexp = "^\\d{5}$", message = "ZIP must be 5 digits")
        String pickupZip,

        @Email(message = "Invalid email format")
        String customerEmail,

        String reasonCode,
        String orderId
) {
    public ReturnRequest {
        if (quantity < 1) {
            // We don't necessarily need to throw here if @Min is doing the job
        }
    }
}