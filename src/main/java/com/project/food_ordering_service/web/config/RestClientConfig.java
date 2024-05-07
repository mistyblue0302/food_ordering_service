package com.project.food_ordering_service.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClientConfig() {
        RestClient restClient = RestClient.builder()
            .baseUrl("localhost")
            .defaultHeader(HttpHeaders.AUTHORIZATION)
            .build();
        return restClient;
    }

}
