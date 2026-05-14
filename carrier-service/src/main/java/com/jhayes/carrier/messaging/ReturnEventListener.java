package com.jhayes.carrier.messaging;

import com.jhayes.returns.event.ReturnInitiatedEvent; // From common-models!
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReturnEventListener {

    @KafkaListener(topics = "return-events.v1", groupId = "carrier-group")
    public void handleReturn(ReturnInitiatedEvent event) {
        String tid = event.traceId();

        System.out.println("\n🎫 [" + tid + "] CARRIER SERVICE: Event Received!");
        System.out.println("[" + tid + "] Generating Label for: " + event.returnId());
        System.out.println("[" + tid + "] MOCK: Shipping label created and sent to S3.");
        System.out.println("------------------------------------------\n");
    }
}