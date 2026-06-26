package com.Forensics.CCIMS.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private String action;

    private String entityType;

    private String entityId;

    private String performedBy;

    private LocalDateTime timestamp;
}