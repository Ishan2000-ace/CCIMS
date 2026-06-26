package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
