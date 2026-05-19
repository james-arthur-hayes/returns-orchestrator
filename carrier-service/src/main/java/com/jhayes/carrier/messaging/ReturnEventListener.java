package com.jhayes.carrier.messaging;

import com.jhayes.returns.event.ReturnInitiatedEvent;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnEventListener {

    private final Tracer tracer; // Wired automatically by Micrometer Tracing

    @KafkaListener(topics = "return-events.v1", groupId = "carrier-group")
    public void handleReturn(ConsumerRecord<String, ReturnInitiatedEvent> record) {
        String incomingTraceId = null;

        // Extract the shared trace header from the incoming network metadata packet
        Header traceHeader = record.headers().lastHeader("x-b3-traceid");
        if (traceHeader != null) {
            incomingTraceId = new String(traceHeader.value(), StandardCharsets.UTF_8);
        }

        ReturnInitiatedEvent event = record.value();

        // Safety fallback to payload traceId if network header extraction fails
        if (incomingTraceId == null && event != null) {
            incomingTraceId = event.traceId();
        }

        // Initialize the tracing boundary for the Carrier microservice JVM scope
        Span nextSpan;
        if (incomingTraceId != null) {
            nextSpan = tracer.nextSpan().start();
        } else {
            nextSpan = tracer.nextSpan().name("fallback-carrier-span").start();
        }

        // Push the trace context into the active execution thread pool
        try (Tracer.SpanInScope ws = tracer.withSpan(nextSpan)) {
            if (event == null) {
                log.warn("[CARRIER-SERVICE] Received an empty or un-parsable Kafka payload envelope.");
                return;
            }

            // High-visibility, reader-friendly production logging statements matching bracket format
            log.info("[CARRIER-SERVICE] Asynchronous Kafka event received for processing.");
            log.info("[CARRIER-SERVICE] Generating logistics validation label for Return ID: [{}]", event.returnId());
            log.info("[CARRIER-SERVICE] MOCK outbound cloud bucket stream -> Shipping label successfully created and uploaded to S3.");

        } catch (Exception e) {
            log.error("[CARRIER-SERVICE-ERROR] Operational exception thrown during label generation stream: {}", e.getMessage(), e);
        } finally {
            // Tear down the trace span block to prevent memory contamination leaks
            nextSpan.end();
        }
    }
}