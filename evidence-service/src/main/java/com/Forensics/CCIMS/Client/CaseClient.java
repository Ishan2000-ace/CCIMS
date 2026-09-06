package com.Forensics.CCIMS.Client;

import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CaseClient {

    private final RestClient restClient;

    public CaseClient(
            RestClient.Builder builder,
            @Value("${case-service.url}") String caseServiceUrl) {

        this.restClient = builder
                .baseUrl(caseServiceUrl)
                .build();
    }

    public CaseResponseDTO getCaseById(String caseId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String token =
                (String) authentication.getCredentials();

        return restClient.get()
                .uri("/cases/getcase/{id}", caseId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(CaseResponseDTO.class);
    }
}