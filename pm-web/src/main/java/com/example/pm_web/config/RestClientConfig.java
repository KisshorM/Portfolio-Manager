package com.example.pm_web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Value("${python.api.base-url:http://localhost:8000}")
    private String pythonApiBaseUrl;

    @Value("${python.api.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Value("${python.api.read-timeout-ms:10000}")
    private int readTimeoutMs;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(pythonApiBaseUrl)
                .setConnectTimeout(java.time.Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(java.time.Duration.ofMillis(readTimeoutMs))
                .build();
    }
}
