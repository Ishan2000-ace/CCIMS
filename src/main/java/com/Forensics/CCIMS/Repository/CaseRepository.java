package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.Case;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CaseRepository extends MongoRepository<Case, String> {
    List<Case> findByUserId(String id);
}
