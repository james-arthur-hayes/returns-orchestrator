package com.jhayes.returns.domain.model;

import java.time.LocalDateTime;

// Response DTO for request acknoledgement
public record ReturnResponse(
        String trackingId,
        String status,
        LocalDateTime timestamp
) {}