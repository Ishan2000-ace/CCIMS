package com.Forensics.CCIMS.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String caseId;
    private String userId;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}