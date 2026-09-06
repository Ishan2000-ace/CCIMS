package com.Forensics.CCIMS.Service;


import com.Forensics.CCIMS.Client.UserClient;
import com.Forensics.CCIMS.DTO.CaseRequestDTO;
import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import com.Forensics.CCIMS.DTO.UserResponseDTO;
import com.Forensics.CCIMS.Entity.Case;
//import com.Forensics.CCIMS.Entity.Users;
import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Kafka.CaseEventProducer;
import com.Forensics.CCIMS.Repository.CaseRepository;
//import com.Forensics.CCIMS.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserClient userClient;

//    @Autowired
//    private UserRepository userRepository;

//    @Autowired
//    private AuditLogService auditService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
   private CaseEventProducer caseEventProducer;

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

    public CaseResponseDTO assignCase(String caseId, String investigatorId) {

        // 1. Find the case
        Case caseObj = caseRepository.findById(caseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Case not found"));

        // 2. Ask User Service about the investigator
        UserResponseDTO investigator =
                userClient.getUserById(investigatorId);

        // 3. Make sure the user is actually an investigator
        if (!"INVESTIGATOR".equals(investigator.getRole())) {
            throw new RuntimeException("User is not an investigator");
        }

        // 4. Get the currently authenticated admin
        String adminUsername =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        // 5. Update case
        caseObj.setAssignedInvestigator(investigator.getId());
        caseObj.setAssignedAt(LocalDateTime.now());
        caseObj.setAssignedBy(adminUsername);
        caseObj.setStatus("ASSIGNED");

        // 6. Save
        Case savedCase = caseRepository.save(caseObj);

        CaseResponseDTO response =
                modelMapper.map(savedCase, CaseResponseDTO.class);

        // 7. Publish event
        CaseAssignedEvent event =
                new CaseAssignedEvent(
                        response.getId(),
                        response.getAssignedInvestigator(),
                        adminUsername,
                        LocalDateTime.now()
                );

        caseEventProducer.publishCaseAssignedEvent(event);

        return response;
    }

   public List<CaseResponseDTO> getCaseByInvestigatorId(String id){
        UserResponseDTO user = userClient.getUserById(id);

        List<Case> cases = caseRepository.findByAssignedInvestigator(user.getId());

       return cases.stream().map(casedto->modelMapper.map(casedto, CaseResponseDTO.class)).toList();
  }

    public List<CaseResponseDTO> getAssignedCases() {

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        UserResponseDTO user =
                userClient.getUserByUsername(username);

        return getCaseByInvestigatorId(user.getId());
    }
}
