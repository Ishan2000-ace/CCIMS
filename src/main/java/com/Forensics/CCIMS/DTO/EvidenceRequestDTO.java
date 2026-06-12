package com.Forensics.CCIMS.DTO;

import lombok.Data;

@Data
public class EvidenceRequestDTO {

    private String caseId;

    private String fileName;

    private String fileHash;

    private String evidenceType;

    private String description;

    private String uploadedBy;
}
