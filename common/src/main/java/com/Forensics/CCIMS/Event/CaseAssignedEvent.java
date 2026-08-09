package com.Forensics.CCIMS.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseAssignedEvent {

    private String caseId;

    private String investigatorId;

    private String assignedBy;

    private LocalDateTime assignedAt;
}