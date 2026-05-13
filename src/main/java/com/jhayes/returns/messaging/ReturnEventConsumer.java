package com.jhayes.returns.messaging;

import com.jhayes.returns.domain.model.ReturnInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReturnEventConsumer {

    @KafkaListener(topics = "return-events.v1", groupId = "returns-group")
    public void consumeReturnEvent(ReturnInitiatedEvent event) {
        // Pull the ID we generated in the Controller
        String tid = event.traceId();

        System.out.println("\n📥 [" + tid + "] KAFKA CONSUMER: Message Received!");
        System.out.println("[" + tid + "] Processing Return ID: " + event.returnId());
        System.out.println("[" + tid + "] Notifying Customer: " + event.customerId());
        System.out.println("[" + tid + "] Status: " + event.status());
        System.out.println("------------------------------------------\n");
    }
}