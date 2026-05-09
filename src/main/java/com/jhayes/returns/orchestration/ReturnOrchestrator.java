package com.jhayes.returns.orchestration;

import com.jhayes.returns.client.CarrierClient;
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
    private final ManifestRepository repository;
    private final CarrierClient carrierClient;

    public Mono<ReturnResponse> processReturn(ReturnRequest request) {
        return returnService.validate(request)
                .flatMap(validReq -> triageFactory.getStrategy(validReq).execute(validReq))
                .flatMap(response -> carrierClient.requestLabel(response.trackingId())
                        .flatMap(labelUrl -> saveToDatabase(request, response, labelUrl))
                );
    }

    private Mono<ReturnResponse> saveToDatabase(ReturnRequest req, ReturnResponse res, String labelUrl) {
        ReturnManifest manifest = ReturnManifest.builder()
                .trackingId(res.trackingId())
                .sku(req.sku())
                .quantity(req.quantity())
                .customerEmail(req.customerEmail())
                .orderId(req.orderId())
                .status(res.status())
                .labelUrl(labelUrl)
                .createdAt(res.timestamp())
                .build();

        return repository.save(manifest)
                .thenReturn(res);
    }

    public Mono<ReturnManifest> getReturnStatus(String trackingId) {
        return repository.findByTrackingId(trackingId)
                .switchIfEmpty(Mono.error(new ReturnNotFoundException(trackingId)));
    }
}