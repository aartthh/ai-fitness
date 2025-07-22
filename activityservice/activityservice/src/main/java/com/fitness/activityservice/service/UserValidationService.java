package com.fitness.activityservice.service;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        try {
            return Boolean.TRUE.equals(userServiceWebClient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (WebClientResponseException e) {
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
            throw new RuntimeException("General error during user validation: " + e.getMessage(), e);
        }
    }


}
