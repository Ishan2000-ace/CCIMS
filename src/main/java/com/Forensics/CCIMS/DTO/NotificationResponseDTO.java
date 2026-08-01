package com.Forensics.CCIMS.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {

    private String id;

    private String title;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;
}