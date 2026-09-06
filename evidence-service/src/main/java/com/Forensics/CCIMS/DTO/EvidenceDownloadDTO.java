package com.Forensics.CCIMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class EvidenceDownloadDTO {

    private Resource resource;
    private String fileName;
    private String fileType;
}