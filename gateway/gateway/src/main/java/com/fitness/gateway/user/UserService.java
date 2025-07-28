package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {
        return userServiceWebClient.get()
                .uri("api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClient error during user validation for userId {}: {}", userId, e.getMessage());
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new RuntimeException("User not found: " + userId));
                    } else if (e.getStatusCode().is4xxClientError()) {
                        return Mono.error(new RuntimeException("Client error: " + e.getMessage()));
                    } else if (e.getStatusCode().is5xxServerError()) {
                        return Mono.error(new RuntimeException("Server error: " + e.getMessage()));
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage()));
                    }
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("General error during user validation for userId {}: {}", userId, e.getMessage(), e);
                    return Mono.error(new RuntimeException("General error during user validation: " + e.getMessage(), e));
                });
    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
        return userServiceWebClient.post()
                .uri("api/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnSuccess(response ->
                        log.info("User registration successful for email: {}", request.getEmail()))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClient error during Registration API for email {}: {}", request.getEmail(), e.getMessage());
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("Bad Request for email: " + request.getEmail()));
                    } else if (e.getStatusCode().is4xxClientError()) {
                        return Mono.error(new RuntimeException("Client error: " + e.getMessage()));
                    } else if (e.getStatusCode().is5xxServerError()) {
                        return Mono.error(new RuntimeException("Server error: " + e.getMessage()));
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage()));
                    }
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during registration: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException("Unexpected registration error", e));
                });
    }

}
