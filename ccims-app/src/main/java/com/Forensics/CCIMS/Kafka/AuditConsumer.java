package com.Forensics.CCIMS.Kafka;

import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import com.Forensics.CCIMS.Service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditLogService auditService;


    @KafkaListener(
            topics = "case-events",
            groupId = "audit-group"
    )
    public void consume(CaseAssignedEvent event) {

        log.info("Received Event : {}", event);

        auditService.log(
                event.getCaseId(),
                event.getAssignedBy(),
                "CASE_ASSIGNED",
                "Case assigned to investigator " + event.getInvestigatorId(),
                event.getInvestigatorId()
        );

    }
}