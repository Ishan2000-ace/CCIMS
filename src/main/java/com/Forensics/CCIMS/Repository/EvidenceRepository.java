package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.Evidence;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EvidenceRepository extends MongoRepository<Evidence, String> {

    List<Evidence> findByCaseId(String caseId);
}
