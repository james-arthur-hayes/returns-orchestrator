package com.jhayes.returns.domain.model;

import java.time.LocalDateTime;

// Response DTO for request acknowledgement
public record ReturnResponse(
        String trackingId,
        String status,
        String labelUrl,
        Long timestamp
) {}