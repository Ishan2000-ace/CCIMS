package com.Forensics.CCIMS.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {

    private String id;

    private String userId;

    private String message;

    private String type;

    private boolean read;

    private LocalDateTime createdAt;
}
