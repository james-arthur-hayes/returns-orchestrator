package com.jhayes.returns.orchestration;

import com.jhayes.returns.domain.model.ReturnRequest;
import com.jhayes.returns.domain.model.ReturnResponse;
import com.jhayes.returns.domain.service.ReturnService;
import com.jhayes.returns.strategy.factory.TriageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReturnOrchestrator {

    private final TriageFactory triageFactory;
    private final ReturnService returnService;

    public Mono<ReturnResponse> processReturn(ReturnRequest request) {
        return returnService.validate(request) // Delegate the "HAYE" check to the service
                .flatMap(validReq -> triageFactory.getStrategy(validReq).execute(validReq));
    }
}