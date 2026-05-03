package com.jhayes.returns.api;

import com.jhayes.returns.domain.model.ReturnRequest;
import com.jhayes.returns.domain.model.ReturnResponse;
import com.jhayes.returns.orchestration.ReturnOrchestrator;
import com.jhayes.returns.repository.ReturnManifest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/returns")
public class ReturnController {

    private final ReturnOrchestrator orchestrator;

    public ReturnController(ReturnOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping
    public Mono<ReturnResponse> initiateReturn(@RequestBody Mono<ReturnRequest> requestMono) {
        // flatMap is the "Senior" way to chain asynchronous operations.
        // We take the incoming request stream, pass it to the orchestrator,
        // and return the resulting response stream.
        return requestMono
                .flatMap(orchestrator::processReturn);
    }

    @GetMapping("/{trackingId}")
    public Mono<ResponseEntity<ReturnManifest>> getReturn(@PathVariable String trackingId) {
        return orchestrator.getReturnStatus(trackingId)
                .map(ResponseEntity::ok) // If found, return 200 OK with the data
                .onErrorReturn(ResponseEntity.notFound().build()); // If error/not found, return 404
    }
}