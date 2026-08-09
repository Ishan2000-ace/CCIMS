package com.Forensics.CCIMS.Service;

import com.Forensics.CCIMS.DTO.NotificationResponseDTO;
import com.Forensics.CCIMS.Entity.Notification;
import com.Forensics.CCIMS.Entity.Users;
import com.Forensics.CCIMS.Event.CaseAssignedEvent;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.NotificationRepository;
import com.Forensics.CCIMS.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    public void createNotification(CaseAssignedEvent event) {

        log.info("Creating notification...");

        Notification notification = new Notification();


        notificationRepository.save(notification);

        log.info("Notification stored in MongoDB");
    }

    private String getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Users user = userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new ResourceNotFoundException("user not found"));

        return user.getId();
    }

    public List<NotificationResponseDTO> getMyNotifications() {

        String userid = getCurrentUser();

        List<Notification> notifications =
                notificationRepository.findByUserId(userid);

        return notifications.stream()
                .map(notification ->
                        modelMapper.map(
                                notification,
                                NotificationResponseDTO.class))
                .toList();
    }



    public NotificationResponseDTO markAsRead(String id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found"));

        String currentUserId = getCurrentUser();

        if (!notification.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You cannot access this notification");
        }

        notification.setRead(true);

        Notification saved = notificationRepository.save(notification);

        return modelMapper.map(saved, NotificationResponseDTO.class);
    }

    public List<NotificationResponseDTO> getUnreadNotifications() {

        String userId = getCurrentUser();

        return notificationRepository
                .findByUserIdAndReadFalse(userId)
                .stream()
                .map(notification ->
                        modelMapper.map(notification, NotificationResponseDTO.class))
                .toList();
    }

}