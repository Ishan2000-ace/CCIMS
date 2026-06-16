package com.Forensics.CCIMS.Service;


import com.Forensics.CCIMS.DTO.CaseRequestDTO;
import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import com.Forensics.CCIMS.Entity.Case;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.CaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CaseResponseDTO createCase(CaseRequestDTO caseRequest){
        Case cases = modelMapper.map(caseRequest, Case.class);
        cases.setStatus("ACTIVE");
        Case scase = caseRepository.save(cases);

        return modelMapper.map(scase, CaseResponseDTO.class);
    }

    public CaseResponseDTO getCaseById(String id){
        Case fcase = caseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Case not found"));

        return modelMapper.map(fcase, CaseResponseDTO.class);
    }

    public List<CaseResponseDTO> findAllCases(){
        List<Case> caselist = new ArrayList<>(caseRepository.findAll());

         return caselist.stream().map(caseEntity -> modelMapper.map(caseEntity, CaseResponseDTO.class)).toList();


    }


    public CaseResponseDTO updateCase (String id , CaseRequestDTO newcase){
        Case ncase = caseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Case not found"));

        ncase.setSeverity(newcase.getSeverity());
        ncase.setAssignedInvestigator(newcase.getAssignedInvestigator());
        ncase.setTitle(newcase.getTitle());
        ncase.setDescription(newcase.getDescription());
        ncase.setCrimeType(newcase.getCrimeType());

        Case scase = caseRepository.save(ncase);

        return modelMapper.map(scase, CaseResponseDTO.class);
    }

    public CaseResponseDTO deleteCase(String id){
        Case dcase = caseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Case not found"));

         caseRepository.delete(dcase);

         return modelMapper.map(dcase, CaseResponseDTO.class);
    }
}
