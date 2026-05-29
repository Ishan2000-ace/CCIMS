package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.CaseRequestDTO;
import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import com.Forensics.CCIMS.Service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("/createcase")

    public ResponseEntity<CaseResponseDTO> createCase(@RequestBody CaseRequestDTO ncase){
        CaseResponseDTO response = caseService.createCase(ncase);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
