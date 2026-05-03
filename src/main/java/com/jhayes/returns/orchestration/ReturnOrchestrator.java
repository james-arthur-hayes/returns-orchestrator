package com.jhayes.returns.orchestration;

import com.jhayes.returns.domain.model.ReturnRequest;
import com.jhayes.returns.domain.model.ReturnResponse;
import com.jhayes.returns.domain.service.ReturnService;
import com.jhayes.returns.exception.ReturnNotFoundException;
import com.jhayes.returns.repository.ManifestRepository;
import com.jhayes.returns.repository.ReturnManifest;
import com.jhayes.returns.strategy.factory.TriageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReturnOrchestrator {

    private final TriageFactory triageFactory;
    private final ReturnService returnService;
    private final ManifestRepository repository; // Add this!

    public Mono<ReturnResponse> processReturn(ReturnRequest request) {
        return returnService.validate(request)
                .flatMap(validReq -> triageFactory.getStrategy(validReq).execute(validReq))
                .flatMap(response -> saveToDatabase(request, response)); // Chained save
    }

    private Mono<ReturnResponse> saveToDatabase(ReturnRequest req, ReturnResponse res) {
        ReturnManifest manifest = ReturnManifest.builder()
                .trackingId(res.trackingId())
                .sku(req.sku())
                .quantity(req.quantity())
                .customerEmail(req.customerEmail())
                .orderId(req.orderId())
                .status(res.status())
                .createdAt(res.timestamp())
                .build();

        return repository.save(manifest)
                .thenReturn(res); // After the save finishes, send the original response back
    }

    public Mono<ReturnManifest> getReturnStatus(String trackingId) {
        return repository.findByTrackingId(trackingId)
                .switchIfEmpty(Mono.error(new ReturnNotFoundException(trackingId)));
    }
}