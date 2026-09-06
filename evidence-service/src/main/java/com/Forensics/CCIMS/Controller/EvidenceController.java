package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.EvidenceDownloadDTO;
import com.Forensics.CCIMS.DTO.EvidenceRequestDTO;
import com.Forensics.CCIMS.DTO.EvidenceResponseDTO;
import com.Forensics.CCIMS.Service.EvidenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/evidence")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    @PostMapping("/create")
    public ResponseEntity<EvidenceResponseDTO> createEvidence(
            @Valid @RequestBody EvidenceRequestDTO request) {

        EvidenceResponseDTO response =
                evidenceService.createEvidence(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/{id}")
    public ResponseEntity<EvidenceResponseDTO> getEvidenceById(
            @PathVariable String id) {

        EvidenceResponseDTO response =
                evidenceService.getEvidenceById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<EvidenceResponseDTO>> getEvidenceByCaseId(
            @PathVariable String caseId) {

        List<EvidenceResponseDTO> response =
                evidenceService.getEvidenceByCaseId(caseId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    @GetMapping("/my-evidence")
    public ResponseEntity<List<EvidenceResponseDTO>> getMyEvidence() {

        String username =
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        List<EvidenceResponseDTO> response =
                evidenceService.getEvidenceByUploader(username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<EvidenceResponseDTO> deleteEvidence(
            @PathVariable String id) {

        EvidenceResponseDTO response =
                evidenceService.deleteEvidence(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<EvidenceResponseDTO> uploadEvidence(
            @RequestParam("caseId") String caseId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") MultipartFile file) {

        EvidenceRequestDTO request = new EvidenceRequestDTO();
        request.setCaseId(caseId);
        request.setDescription(description);

        EvidenceResponseDTO response =
                evidenceService.uploadEvidence(request, file);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/test-post")
    public ResponseEntity<String> testPost() {
        return ResponseEntity.ok("POST authorization works");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadEvidence(
            @PathVariable String id) {

        EvidenceDownloadDTO download =
                evidenceService.downloadEvidence(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                download.getFileName() + "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                download.getFileType()
                        )
                )
                .body(download.getResource());
    }
}