package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.CaseRequestDTO;
import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import com.Forensics.CCIMS.Service.CaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PreAuthorize("hasAnyRole('ADMIN','INVESTIGATOR')")
    @PostMapping("/createcase")
    public ResponseEntity<CaseResponseDTO> createCase(@Valid @RequestBody CaseRequestDTO ncase){
        CaseResponseDTO response = caseService.createCase(ncase);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INVESTIGATOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CaseResponseDTO> updateCase(@PathVariable String id, @RequestBody CaseRequestDTO ucase){
        CaseResponseDTO responseDTO = caseService.updateCase(id, ucase);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/getcase/{id}")
    public ResponseEntity<CaseResponseDTO>getCaseById(@PathVariable String id){
        CaseResponseDTO responseDTO = caseService.getCaseById(id);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCase/{id}")
    public ResponseEntity<CaseResponseDTO> deleteCase(@PathVariable String id){
        CaseResponseDTO responseDTO = caseService.deleteCase(id);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllCases")
    public ResponseEntity<List<CaseResponseDTO>> getAllCases(){
        List<CaseResponseDTO> caselist = caseService.findAllCases();

        return new ResponseEntity<>(caselist, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign/caseId/investigatorId")
    public ResponseEntity<CaseResponseDTO>assignCase(@Valid @PathVariable String caseId, @Valid @PathVariable String investigatorId){
        CaseResponseDTO response = caseService.assignCase(caseId, investigatorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('INVESTIGATOR', 'ANALYST')")
    @GetMapping("/getassignedcases")
    public ResponseEntity<List<CaseResponseDTO>> getAssignedCases(){
        List<CaseResponseDTO> responseDTOList = caseService.getAssignedCases();

        return new ResponseEntity<>(responseDTOList, HttpStatus.OK);
    }
}
