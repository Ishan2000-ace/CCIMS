package com.Forensics.CCIMS.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CaseRequestDTO {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotBlank(message = "Crime type is required")
    private String crimeType;

    @NotBlank(message = "severity is required")
    private String severity;

    @NotBlank(message = "Assigned Investigator is required")
    private String assignedInvestigator;
}
