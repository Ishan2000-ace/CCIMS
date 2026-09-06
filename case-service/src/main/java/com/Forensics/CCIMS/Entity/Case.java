package com.Forensics.CCIMS.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cases")
public class Case {

    @Id
    private String id;

    private String title;

    private String description;

    private String crimeType;

    private String severity;

    private String status;

    private String assignedInvestigator;

    private String assignedBy;

    private LocalDateTime assignedAt;

    private LocalDateTime createdAt;
}