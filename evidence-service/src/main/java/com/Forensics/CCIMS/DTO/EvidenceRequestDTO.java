package com.Forensics.CCIMS.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvidenceRequestDTO {

    @NotBlank
    private String caseId;

    private String description;
}