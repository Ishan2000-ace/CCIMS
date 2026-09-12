package com.Forensics.CCIMS.Kafka;

import com.Forensics.CCIMS.Entity.Notification;
import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import com.Forensics.CCIMS.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseAssignedEventConsumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(
            topics = "case-events",
            groupId = "notification-service"
    )
    public void consumeCaseAssignedEvent(
            CaseAssignedEvent event) {

        Notification notification = new Notification();

        notification.setUserId(event.getInvestigatorId());

        notification.setMessage(
                "Case " + event.getCaseId() +
                        " has been assigned to you"
        );

        notification.setType("CASE_ASSIGNED");

        notification.setRead(false);

        notification.setCreatedAt(event.getAssignedAt());

        notificationRepository.save(notification);
    }
}