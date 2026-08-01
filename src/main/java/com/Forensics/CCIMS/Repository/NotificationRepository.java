package com.Forensics.CCIMS.Repository;

import com.Forensics.CCIMS.Entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository
        extends MongoRepository<Notification, String> {

    List<Notification> findByUserId(String userId);

    List<Notification> findByUserIdAndReadFalse(String userId);
}