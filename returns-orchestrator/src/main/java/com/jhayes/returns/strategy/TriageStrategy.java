package com.jhayes.returns.strategy;

import com.jhayes.returns.event.ReturnRequest;
import com.jhayes.returns.event.ReturnResponse;
import reactor.core.publisher.Mono;

public interface TriageStrategy {
    boolean isApplicable(ReturnRequest request);
    Mono<ReturnResponse> execute(ReturnRequest request);
}