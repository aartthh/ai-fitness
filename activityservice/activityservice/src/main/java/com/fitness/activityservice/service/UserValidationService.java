package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        try {
            Boolean result = userServiceWebClient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .timeout(Duration.ofSeconds(5)) // Add timeout
                    .block();

            return Boolean.TRUE.equals(result);

        } catch (WebClientResponseException e) {
            log.error("WebClient error during user validation for userId {}: {}", userId, e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User not found: " + userId);
            } else if (e.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Client error: " + e.getMessage());
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Server error: " + e.getMessage());
            } else {
                throw new RuntimeException("Unexpected error: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("General error during user validation for userId {}: {}", userId, e.getMessage(), e);

            // Option: Return false instead of throwing exception for graceful degradation
            // return false;

            throw new RuntimeException("General error during user validation: " + e.getMessage(), e);
        }
    }
}