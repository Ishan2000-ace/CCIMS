package com.Forensics.CCIMS.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EvidenceResponseDTO {

    private String id;

    private String caseId;

    private String fileName;

    private String fileType;

    private long fileSize;

    private String description;

    private String uploadedBy;

    private LocalDateTime uploadedAt;
}