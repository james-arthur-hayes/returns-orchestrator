package com.jhayes.returns.orchestration;

import com.jhayes.returns.domain.model.ReturnInitiatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReturnEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "return-events.v1";

    public ReturnEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReturnEvent(ReturnInitiatedEvent event) {
        kafkaTemplate.send(TOPIC, event.returnId(), event);
        System.out.println("🚀 Asynchronous Event Fired to Kafka: " + event.returnId());
    }
}