package com.jhayes.returns.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mock-carrier")
public class MockCarrierController {

    @PostMapping("/generate")
    public Mono<Map<String, String>> generateLabel(@RequestBody Map<String, String> request) {
        String trackingId = request.get("trackingId");

        return Mono.just(Map.of(
                "labelUrl", "https://carrier.com/labels/" + trackingId + ".pdf",
                "status", "SUCCESS"
        )).delayElement(Duration.ofMillis(500));
    }
}