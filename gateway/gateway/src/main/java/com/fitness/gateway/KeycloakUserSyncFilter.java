package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        RegisterRequest registerRequest = getUserDetails(token);

        if(userId == null){
            userId = registerRequest.getKeycloakId();
        }
        if (userId != null && token != null) {
            String finalUserId = userId;
            String finalUserId1 = userId;
            return userService.validateUser(userId)
                    .flatMap(exist -> {
                        if (!exist) {

                            if (registerRequest != null) {
                                return userService.registerUser(registerRequest)
                                        .doOnSuccess(unused -> log.info("User registered successfully"))
                                        .doOnError(e -> log.error("Failed to register user: {}", e.getMessage()))
                                        .then(Mono.empty());
                            } else {
                                log.warn("Failed to extract user details from token.");
                                return Mono.empty();
                            }
                        } else {
                            log.info("User already exists with userId: {}", finalUserId);
                            return Mono.empty();
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", finalUserId1)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        log.warn("Missing headers in request: userId={}, token={}", userId, token != null);
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            if (token.toLowerCase().startsWith("bearer ")) {
                token = token.substring(7).trim();
            }

            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(jwtClaimsSet.getStringClaim("email"));
            registerRequest.setKeycloakId(jwtClaimsSet.getStringClaim("sub"));
            registerRequest.setPassword("dummy@123123");
            registerRequest.setFirstName(jwtClaimsSet.getStringClaim("given_name"));
            registerRequest.setLastName(jwtClaimsSet.getStringClaim("family_name"));
            return registerRequest;
        } catch (Exception e) {
            log.error("Error parsing token for user details: {}", e.getMessage(), e);
            return null;
        }
    }
}
