package com.project.food_ordering_service.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient getRestClient() {
        RestClient restClient = RestClient.builder()
            .defaultHeader(HttpHeaders.AUTHORIZATION)
            .build();
        return restClient;
    }
}