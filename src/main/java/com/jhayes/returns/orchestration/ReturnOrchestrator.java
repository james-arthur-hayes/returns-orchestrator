package com.jhayes.returns.orchestration;

import com.jhayes.returns.domain.ReturnRequest;
import com.jhayes.returns.domain.ReturnResponse;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReturnOrchestrator {

    public Mono<ReturnResponse> processReturn(ReturnRequest request) {
        // For now, we just "echo" the request back to prove the plumbing works.
        // Later, this is where the Decision Engine logic will live.
        return Mono.just(new ReturnResponse(
                UUID.randomUUID().toString(),
                "RECEIVED",
                LocalDateTime.now()
        ));
    }
}