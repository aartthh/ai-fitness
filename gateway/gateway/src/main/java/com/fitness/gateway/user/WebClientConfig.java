package com.fitness.gateway.user;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder loadBalancedWebClientBuilder) {
        System.out.println("✅ Creating USER-SERVICE WebClient with LoadBalanced");
        return loadBalancedWebClientBuilder
                .baseUrl("http://USER-SERVICE")  // Eureka will resolve this
                .build();
    }
}
