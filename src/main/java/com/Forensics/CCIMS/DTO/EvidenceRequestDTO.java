package com.Forensics.CCIMS.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvidenceRequestDTO {

    @NotBlank(message = "caseid is required")
    private String caseId;

    @NotBlank(message = "filename is required")
    private String fileName;

    @NotBlank(message = "file hash is required")
    private String fileHash;

    @NotBlank(message = "Evidence type is required")
    private String evidenceType;

    private String description;

    @NotBlank(message = "Uploaded by user is required")
    private String uploadedBy;
}
