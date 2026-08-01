package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.NotificationResponseDTO;
import com.Forensics.CCIMS.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ADMIN','INVESTIGATOR','ANALYST')")
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {

        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    @PreAuthorize("hasAnyRole('ADMIN','INVESTIGATOR','ANALYST')")
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable String id) {

        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

}