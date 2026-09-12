package com.Forensics.CCIMS.Service;

import com.Forensics.CCIMS.DTO.NotificationResponseDTO;
import com.Forensics.CCIMS.Entity.Notification;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<NotificationResponseDTO> getNotificationsByUserId(String userId) {

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notification ->
                        modelMapper.map(
                                notification,
                                NotificationResponseDTO.class
                        ))
                .toList();
    }

    public List<NotificationResponseDTO> getUnreadNotificationsByUserId(
            String userId) {

        return notificationRepository
                .findByUserIdAndReadFalse(userId)
                .stream()
                .map(notification ->
                        modelMapper.map(
                                notification,
                                NotificationResponseDTO.class
                        ))
                .toList();
    }

    public NotificationResponseDTO markAsRead(
            String notificationId,
            String userId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                ));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You are not allowed to modify this notification"
            );
        }

        notification.setRead(true);

        Notification updated =
                notificationRepository.save(notification);

        return modelMapper.map(
                updated,
                NotificationResponseDTO.class
        );
    }
}