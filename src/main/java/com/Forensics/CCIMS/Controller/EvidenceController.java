package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.EvidenceRequestDTO;
import com.Forensics.CCIMS.DTO.EvidenceResponseDTO;
import com.Forensics.CCIMS.Service.EvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evidence")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    @GetMapping("/getbyid/{id}")

    public ResponseEntity<List<EvidenceResponseDTO>> getEvidenceById(@PathVariable String id){
        List<EvidenceResponseDTO> response = evidenceService.getEvidenceByCase(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create/{id}")

    public ResponseEntity<EvidenceResponseDTO> createEvidence(@PathVariable String id, @RequestBody EvidenceRequestDTO Evidence){
        EvidenceResponseDTO response = evidenceService.createEvidence(id,Evidence);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")

    public ResponseEntity<EvidenceResponseDTO> updateEvidence(@PathVariable String id , @RequestBody EvidenceRequestDTO evidence){
        EvidenceResponseDTO response = evidenceService.updateEvidence(id, evidence);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")

    public ResponseEntity<EvidenceResponseDTO> deleteEvidence(@PathVariable String id){
        EvidenceResponseDTO response = evidenceService.deleteEvidence(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
