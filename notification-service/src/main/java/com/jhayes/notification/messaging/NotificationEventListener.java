package com.jhayes.notification.messaging;

import com.jhayes.returns.event.ReturnInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventListener {

    @KafkaListener(topics = "return-events.v1", groupId = "notification-group")
    public void handleNotification(ReturnInitiatedEvent event) {
        String tid = event.traceId();

        System.out.println("\n📧 [" + tid + "] NOTIFICATION SERVICE: Email Request Received!");
        System.out.println("[" + tid + "] Sending confirmation to customer for Order: " + event.orderId());
        System.out.println("[" + tid + "] MOCK: SMTP Email sent successfully via SendGrid.");
        System.out.println("------------------------------------------\n");
    }
}