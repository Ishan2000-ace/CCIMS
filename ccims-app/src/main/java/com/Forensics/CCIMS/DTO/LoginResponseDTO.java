package com.Forensics.CCIMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    String userId;

    String username;

    String role;

    String token;
}
