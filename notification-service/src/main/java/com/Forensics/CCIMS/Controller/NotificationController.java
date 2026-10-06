package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.Client.UserClient;
import com.Forensics.CCIMS.DTO.NotificationResponseDTO;
import com.Forensics.CCIMS.DTO.UserResponseDTO;
import com.Forensics.CCIMS.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserClient userClient;

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        UserResponseDTO user =
                userClient.getUserByUsername(username);

        List<NotificationResponseDTO> notifications =
                notificationService.getNotificationsByUserId(user.getId());

        return new ResponseEntity<>(
                notifications,
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications() {

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        UserResponseDTO user =
                userClient.getUserByUsername(username);

        List<NotificationResponseDTO> notifications =
                notificationService.getUnreadNotificationsByUserId(user.getId());

        return new ResponseEntity<>(
                notifications,
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR', 'ANALYST')")
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable String id) {

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        UserResponseDTO user =
                userClient.getUserByUsername(username);

        NotificationResponseDTO response =
                notificationService.markAsRead(
                        id,
                        user.getId()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }





}