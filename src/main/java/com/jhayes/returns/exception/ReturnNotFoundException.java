package com.jhayes.returns.exception;

// A custom exception makes your logs and handlers much cleaner
public class ReturnNotFoundException extends RuntimeException {
    public ReturnNotFoundException(String trackingId) {
        super("Return manifest not found for tracking ID: " + trackingId);
    }
}