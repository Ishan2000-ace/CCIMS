package com.Forensics.CCIMS.DTO;

import lombok.Data;

@Data
public class CaseRequestDTO {
    private String title;

    private String description;

    private String crimeType;

    private String severity;

    private String assignedInvestigator;
}
