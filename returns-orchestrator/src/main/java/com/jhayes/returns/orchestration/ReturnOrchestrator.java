package com.jhayes.returns.orchestration;

import com.jhayes.returns.client.CarrierClient;
import com.jhayes.returns.event.ReturnInitiatedEvent;
import com.jhayes.returns.event.ReturnRequest;
import com.jhayes.returns.event.ReturnResponse;
import com.jhayes.returns.domain.service.ReturnService;
import com.jhayes.returns.exception.ReturnNotFoundException;
import com.jhayes.returns.repository.ManifestRepository;
import com.jhayes.returns.repository.ReturnManifest;
import com.jhayes.returns.strategy.factory.TriageFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReturnOrchestrator {

    private final TriageFactory triageFactory;
    private final ReturnService returnService;
    private final ManifestRepository repository;
    private final CarrierClient carrierClient;
    private final ReturnEventPublisher eventPublisher;

    public Mono<ReturnResponse> processReturn(ReturnRequest request, String traceId) {
        return Mono.deferContextual(context -> {
            // [ORCHESTRATOR-API] - Captures web-layer ingress
            log.info("[ORCHESTRATOR-API] Ingesting incoming return request for Order: [{}], SKU: [{}]",
                    request.orderId(), request.sku());

            return returnService.validate(request)
                    .doOnNext(validReq -> log.info("[ORCHESTRATOR-VALIDATION] Data verification rules cleared for Order: [{}]",
                            validReq.orderId()))

                    // [ORCHESTRATOR-CORE] - Core business triage matrix calculation
                    .flatMap(validReq -> triageFactory.getStrategy(validReq).execute(validReq))
                    .doOnNext(response -> log.info("[ORCHESTRATOR-CORE] Return routing strategy computed -> Assigned Tracking ID: [{}] with Status: [{}]",
                            response.trackingId(), response.status()))

                    // [ORCHESTRATOR-CARRIER] - Interacting with external 3PL integrations
                    .flatMap(response -> carrierClient.requestLabel(response.trackingId())
                            .doOnNext(labelUrl -> log.info("[ORCHESTRATOR-CARRIER] External 3PL carrier label generated successfully -> URL: [{}]", labelUrl))
                            .flatMap(labelUrl -> saveToDatabase(request, response, labelUrl))
                    )

                    // [ORCHESTRATOR-KAFKA] - Outbound event stream dispatch
                    .doOnSuccess(res -> {
                        log.info("[ORCHESTRATOR-KAFKA] Broadcasting ReturnInitiatedEvent downstream for Tracking ID: [{}]", res.trackingId());
                        eventPublisher.publishReturnEvent(
                                new ReturnInitiatedEvent(
                                        res.trackingId(),
                                        request.orderId(),
                                        request.customerId(),
                                        request.customerEmail(),
                                        request.sku(),
                                        traceId
                                )
                        );
                    })

                    // Error Boundary Handling
                    .doOnError(error -> log.error("[ORCHESTRATOR-ERROR] Operational pipeline friction encountered for Order: [{}]. Reason: {}",
                            request.orderId(), error.getMessage()));
        });
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
                .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(res.timestamp()), ZoneId.systemDefault()))
                .build();

        return repository.save(manifest)
                // [ORCHESTRATOR-DATABASE] - Persistent audit storage operations
                .doOnSuccess(savedManifest -> log.info("[ORCHESTRATOR-DATABASE] Manifest audit record permanently persisted for Tracking ID: [{}]",
                        savedManifest.getTrackingId()))
                .thenReturn(res);
    }

    public Mono<ReturnManifest> getReturnStatus(String trackingId) {
        return Mono.defer(() -> {
            // [ORCHESTRATOR-LOOKUP] - Read query operations
            log.info("[ORCHESTRATOR-LOOKUP] Executing operational trace for Tracking ID: [{}]", trackingId);

            return repository.findByTrackingId(trackingId)
                    .doOnNext(manifest -> log.info("[ORCHESTRATOR-LOOKUP] Manifest record located. Active State: [{}]", manifest.getStatus()))
                    .switchIfEmpty(Mono.defer(() -> {
                        log.warn("[ORCHESTRATOR-LOOKUP-WARN] Database search yielded zero results for tracking token: [{}]", trackingId);
                        return Mono.error(new ReturnNotFoundException(trackingId));
                    }));
        });
    }
}