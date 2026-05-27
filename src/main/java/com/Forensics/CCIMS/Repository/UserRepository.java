package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {
}
