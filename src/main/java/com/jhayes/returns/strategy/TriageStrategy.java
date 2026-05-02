package com.jhayes.returns.strategy;

import com.jhayes.returns.domain.model.ReturnRequest;
import com.jhayes.returns.domain.model.ReturnResponse;
import reactor.core.publisher.Mono;

public interface TriageStrategy {
    boolean isApplicable(ReturnRequest request);
    Mono<ReturnResponse> execute(ReturnRequest request);
}