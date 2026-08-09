package com.Forensics.CCIMS.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "evidence")
@Data
public class Evidence {
    @Id
    private String id;

    private String caseId;

    private String fileName;

    private String fileHash;

    private String evidenceType;

    private String description;

    private String uploadedBy;

    private LocalDateTime uploadedAt;
}
