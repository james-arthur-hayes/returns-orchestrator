package com.jhayes.returns.domain.model;

public record ReturnInitiatedEvent(
        String returnId,
        String customerId,
        String status,
        String message,
        String traceId
) {}