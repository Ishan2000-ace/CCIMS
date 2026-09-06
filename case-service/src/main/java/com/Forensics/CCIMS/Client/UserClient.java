package com.Forensics.CCIMS.Client;

import com.Forensics.CCIMS.DTO.UserResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            RestClient.Builder builder,
            @Value("${user-service.url}") String userServiceUrl) {

        this.restClient = builder
                .baseUrl(userServiceUrl)
                .build();
    }

    public UserResponseDTO getUserById(String id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String token = (String) authentication.getCredentials();

        return restClient.get()
                .uri("/user/internal/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(UserResponseDTO.class);
    }

    public UserResponseDTO getUserByUsername(String username) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String token = (String) authentication.getCredentials();

        return restClient.get()
                .uri("/user/internal/username/{username}", username)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(UserResponseDTO.class);
    }
}