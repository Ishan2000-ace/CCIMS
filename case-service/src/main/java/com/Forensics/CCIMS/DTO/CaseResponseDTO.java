package com.Forensics.CCIMS.DTO;

import lombok.Data;

@Data
public class CaseResponseDTO {
    private String id;

    private String title;

    private String status;

    private String assignedInvestigator;
}
