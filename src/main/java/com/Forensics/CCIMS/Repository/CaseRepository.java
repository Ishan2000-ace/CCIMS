package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.Case;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseRepository extends MongoRepository<Case, String> {
}
