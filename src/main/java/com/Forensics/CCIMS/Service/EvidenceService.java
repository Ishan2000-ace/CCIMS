package com.Forensics.CCIMS.Service;

import com.Forensics.CCIMS.DTO.EvidenceRequestDTO;
import com.Forensics.CCIMS.DTO.EvidenceResponseDTO;
import com.Forensics.CCIMS.Entity.Evidence;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.EvidenceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenceService {

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EvidenceResponseDTO createEvidence(String caseId, EvidenceRequestDTO evidence){
        Evidence evidence1 = modelMapper.map(evidence, Evidence.class);

        evidence1.setCaseId(caseId);

        evidenceRepository.save(evidence1);

        return modelMapper.map(evidence1, EvidenceResponseDTO.class);
    }

    public List<EvidenceResponseDTO> getEvidenceByCase(String caseId){
        List<Evidence> evidenceList  = evidenceRepository.findByCaseId(caseId);


        return        evidenceList.stream()
                        .map(evidence ->
                                modelMapper.map(
                                        evidence,
                                        EvidenceResponseDTO.class)).toList();
    }

    public EvidenceResponseDTO updateEvidence(String id, EvidenceRequestDTO evidenceRequestDTO){
        Evidence evidence = evidenceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Evidence not Found"));

        evidence.setFileName(evidenceRequestDTO.getFileName());
        evidence.setFileHash(evidenceRequestDTO.getFileHash());
        evidence.setEvidenceType(evidenceRequestDTO.getEvidenceType());
        evidence.setDescription(evidenceRequestDTO.getDescription());
        evidence.setUploadedBy(evidenceRequestDTO.getUploadedBy());

        return modelMapper.map(evidenceRepository.save(evidence), EvidenceResponseDTO.class);
    }

    public EvidenceResponseDTO deleteEvidence(String id){
        Evidence e = evidenceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Evidence not Found"));

        evidenceRepository.delete(e);

        return modelMapper.map(e, EvidenceResponseDTO.class);
    }
}
