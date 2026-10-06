package com.Forensics.CCIMS.Client;

import com.Forensics.CCIMS.DTO.CaseResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class CaseClient {

    private final RestClient restClient;

    public CaseClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://CASE-SERVICE")
                .build();
    }

    @CircuitBreaker(
            name = "caseService",
            fallbackMethod = "caseServiceFallback"
    )
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

    public CaseResponseDTO caseServiceFallback(
            String caseId,
            Throwable throwable) {

        log.error(
                "Case Service unavailable while processing caseId={}. Reason: {}",
                caseId,
                throwable.getMessage(),
                throwable
        );

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Case Service is currently unavailable. Please try again later."
        );
    }
}