package com.Forensics.CCIMS.Kafka;

import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import com.Forensics.CCIMS.Service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "case-events",
            groupId = "notification-group"
    )
    public void consume(CaseAssignedEvent event) {

        log.info("===== Notification Consumer Started =====");

        try {
            log.info("Received notification event : {}", event);

            notificationService.createNotification(event);

            log.info("Notification saved successfully");
        } catch (Exception e) {
            log.error("Notification consumer failed", e);
        }
    }

    @PostConstruct
    public void init() {
        log.info("********NotificationConsumer bean created*********");
    }
}