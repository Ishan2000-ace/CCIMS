package com.Forensics.CCIMS.Service;


import com.Forensics.CCIMS.DTO.CaseRequestDTO;
import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import com.Forensics.CCIMS.Entity.Case;
import com.Forensics.CCIMS.Entity.Users;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.CaseRepository;
import com.Forensics.CCIMS.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

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

    public CaseResponseDTO assignCase(String caseId, String investigatorId){
        Case caseObj = caseRepository.findById(caseId).orElseThrow(()->new ResourceNotFoundException("Case not found"));

        Users Investigator = userRepository.findById(investigatorId).orElseThrow(()->new ResourceNotFoundException("User Not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users admin = userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("Admin not found"));

        if(!Investigator.getRole().equals("INVESTIGATOR")){
            throw new RuntimeException("User is not an investigator");
        }

        caseObj.setAssignedInvestigator(investigatorId);
        caseObj.setAssignedAt(LocalDateTime.now());
        caseObj.setAssignedBy(admin.getId());
        caseObj.setStatus("ASSIGNED");

        return modelMapper.map(caseRepository.save(caseObj), CaseResponseDTO.class);
    }

    public List<CaseResponseDTO> getAssignedCases(){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository
                .findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User Not found"));

        String investigatorId = user.getId();

        return getCaseByInvestigatorId(investigatorId);
    }

    public List<CaseResponseDTO> getCaseByInvestigatorId(String id){
        Users user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));

        List<Case> cases = caseRepository.findByUserId(user.getId());

        return cases.stream().map(casedto->modelMapper.map(casedto, CaseResponseDTO.class)).toList();
    }
}
