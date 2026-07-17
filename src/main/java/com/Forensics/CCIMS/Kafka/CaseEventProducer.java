package com.Forensics.CCIMS.Kafka;

import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaseEventProducer {

    private static final String TOPIC = "case-events";

    private final KafkaTemplate<String, CaseAssignedEvent> kafkaTemplate;

    public void publishCaseAssignedEvent(CaseAssignedEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getCaseId(),
                event
        );

        log.info("Published CaseAssignedEvent : {}", event);
    }
}